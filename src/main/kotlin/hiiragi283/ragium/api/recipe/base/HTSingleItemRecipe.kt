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
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.*

/**
 * 単一のアイテムによるインプットを受け付けるレシピ
 */
abstract class HTSingleItemRecipe(
    group: String,
    val input: SizedIngredient,
    val catalyst: Optional<Ingredient>,
    itemResult: HTItemResult,
) : HTMachineRecipeBase(group) {
    override val itemResults: List<HTItemResult> = listOf(itemResult)

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        if (!this.input.test(input.getItem(0))) return false
        val catalystItem: ItemStack = input.getItem(1)
        return catalyst.map { it.test(catalystItem) }.orElse(catalystItem.isEmpty)
    }

    //    Serializer    //

    class Serializer<T : HTSingleItemRecipe>(private val factory: (String, SizedIngredient, Optional<Ingredient>, HTItemResult) -> T) :
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
            SizedIngredient.STREAM_CODEC,
            HTSingleItemRecipe::input,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC),
            HTSingleItemRecipe::catalyst,
            HTItemResult.STREAM_CODEC,
            { it.itemResults[0] },
            factory,
        )

        override fun codec(): MapCodec<T> = codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
    }
}
