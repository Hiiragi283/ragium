package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.recipe.condition.HTProcessorCatalystCondition
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.*

abstract class HTSingleItemRecipe(
    group: String,
    val input: SizedIngredient,
    val catalyst: Optional<Ingredient>,
    val output: ItemStack,
) : HTMachineRecipeBase(group) {
    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        if (!this.input.test(input.getItem(0))) return false
        return catalyst.map { HTProcessorCatalystCondition(it).test(level, input.pos) }.orElse(true)
    }

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = output.copy()

    //    Serializer    //

    class Serializer<T : HTSingleItemRecipe>(private val factory: (String, SizedIngredient, Optional<Ingredient>, ItemStack) -> T) :
        RecipeSerializer<T> {
        private val codec: MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.GROUP.forGetter(HTSingleItemRecipe::getGroup),
                    HTRecipeCodecs.ITEM_INPUT.forGetter(HTSingleItemRecipe::input),
                    HTRecipeCodecs.CATALYST.forGetter(HTSingleItemRecipe::catalyst),
                    HTRecipeCodecs.ITEM_OUTPUT.forGetter(HTSingleItemRecipe::output),
                ).apply(instance, factory)
        }

        private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTSingleItemRecipe::getGroup,
            SizedIngredient.STREAM_CODEC,
            HTSingleItemRecipe::input,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC),
            HTSingleItemRecipe::catalyst,
            ItemStack.STREAM_CODEC,
            HTSingleItemRecipe::output,
            factory,
        )

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }
}
