package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack

/**
 * 2種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTDoubleItemToItemRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<ItemStack> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class Catalysed(ingredient: HTItemIngredient, catalyst: HTItemCatalyst, result: HTItemResult, progressData: HTProgressData) :
        HTCatalysedSingleRecipe<HTItemIngredient, HTItemResult>(ingredient, catalyst, result, progressData),
        HTDoubleItemToItemRecipe {
        companion object {
            @JvmStatic
            fun <RECIPE : Catalysed> codec(factory: Factory<HTItemIngredient, HTItemResult, RECIPE>): MapCodec<RECIPE> =
                codec(HTItemIngredient.CODEC, HTItemResult.CODEC, factory)

            @JvmStatic
            fun <RECIPE : Catalysed> streamCodec(
                factory: Factory<HTItemIngredient, HTItemResult, RECIPE>,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec(HTItemIngredient.STREAM_CODEC, HTItemResult.STREAM_CODEC, factory)
        }

        override fun test(first: ItemInstance, second: ItemInstance): Boolean = ingredient.test(first) && catalyst.test(second)

        override fun getProgressData(first: ItemInstance, second: ItemInstance): HTProgressData = progressData

        override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()

        override fun getMatchingStack(first: ItemInstance, second: ItemInstance): Pair<ItemInstance, ItemInstance> =
            ingredient.getMatchingStack(first) to catalyst.getMatchingStack(second)
    }
}
