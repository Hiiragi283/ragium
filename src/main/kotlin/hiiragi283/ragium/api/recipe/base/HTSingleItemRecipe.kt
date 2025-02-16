package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import java.util.*

/**
 * 単一のアイテムによるインプットを受け付けるレシピ
 */
abstract class HTSingleItemRecipe(
    group: String,
    val input: HTItemIngredient,
    val catalyst: Optional<Ingredient>,
    itemOutput: HTItemOutput,
) : HTMachineRecipeBase(group) {
    override val itemOutputs: List<HTItemOutput> = listOf(itemOutput)

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        if (!this.input.test(input, 0)) return false
        val catalystItem: ItemStack = input.getItem(1)
        return catalyst.map { it.test(catalystItem) }.orElse(catalystItem.isEmpty)
    }

    //    Serializer    //

    class Serializer<T : HTSingleItemRecipe>(private val factory: (String, HTItemIngredient, Optional<Ingredient>, HTItemOutput) -> T) :
        RecipeSerializer<T> {
        private val codec: MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTRecipeCodecs.group(),
                    HTRecipeCodecs.ITEM_INPUT.forGetter(HTSingleItemRecipe::input),
                    HTRecipeCodecs.CATALYST.forGetter(HTSingleItemRecipe::catalyst),
                    HTRecipeCodecs.itemResult(),
                ).apply(instance, factory)
        }

        private val streamCodec: StreamCodec<RegistryFriendlyByteBuf, T> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTSingleItemRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTSingleItemRecipe::input,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC),
            HTSingleItemRecipe::catalyst,
            HTItemOutput.STREAM_CODEC,
            { it.itemOutputs[0] },
            factory,
        )

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }
}
