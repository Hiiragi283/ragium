package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.serialization.network.nelOf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack

/**
 * 1種類のアイテムから複数のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
interface HTItemToMultiItemRecipe : HTItemToRecipe<List<ItemStack>> {
    fun asSingleOutput(): HTItemToItemRecipe? = null

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class Basic(override val ingredient: HTItemIngredient, val results: Nel<HTItemResult>, override val progressData: HTProgressData) :
        HTItemToMultiItemRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTItemToMultiItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                    HTItemResult.CODEC
                        .compactNelFieldOf(HTConstants.RESULT, HTConstants.RESULTS, 4)
                        .forGetter(Basic::results),
                    HTProgressRecipe.Holder.progressCodec(),
                ).apply(instance, factory::create)
            }

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: HTItemToMultiItemRecipeBuilder.Factory<RECIPE>): StreamCodec<RegistryFriendlyByteBuf, RECIPE> =
                StreamCodec.composite(
                    HTItemIngredient.STREAM_CODEC,
                    Basic::ingredient,
                    HTItemResult.STREAM_CODEC.nelOf(),
                    Basic::results,
                    HTProgressData.STREAM_CODEC,
                    Basic::progressData,
                    factory::create,
                )
        }

        override fun asSingleOutput(): HTItemToRecipe.BasicItem = HTItemToRecipe.BasicItem(ingredient, results.head, progressData)

        override fun getProgressData(input: ItemInstance): HTProgressData = progressData

        override fun apply(input: ItemInstance): List<ItemStack> = results.map(HTItemResult::create)

        override fun getMatchingStack(input: ItemInstance): ItemInstance = ingredient.getMatchingStack(input)
    }
}
