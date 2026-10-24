package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
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
 * 複数のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
interface HTSizedShapelessRecipe :
    HTRecipePredicates.MultiItem,
    HTRecipeFactories.MultiItemTo<ItemStack> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    open class Basic(val ingredients: Nel<HTItemIngredient>, val result: HTItemResult, override val progressData: HTProgressData) :
        HTSizedShapelessRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: (Nel<HTItemIngredient>, HTItemResult, HTProgressData) -> RECIPE): MapCodec<RECIPE> =
                HTCodecs.recordMap { instance ->
                    instance.group(
                        HTItemIngredient.CODEC
                            .compactNelFieldOf(HTConstants.INGREDIENT, HTConstants.INGREDIENTS)
                            .forGetter(Basic::ingredients),
                        HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                        HTProgressData.CODEC.forGetter(Basic::progressData),
                    ).apply(instance, factory)
                }

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(
                factory: (Nel<HTItemIngredient>, HTItemResult, HTProgressData) -> RECIPE,
            ): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC.nelOf(),
                Basic::ingredients,
                HTItemResult.STREAM_CODEC,
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory,
            )
        }

        override fun test(input: List<ItemInstance>): Boolean = HTIngredientHelper.match(ingredients, input)

        override fun apply(input: List<ItemInstance>): ItemStack = result.create()

        override fun getMatchingStack(input: List<ItemInstance>): List<ItemInstance> = HTIngredientHelper.getMatchingStacks(ingredients, input)

        override fun getProgressData(input: List<ItemInstance>): HTProgressData = progressData
    }
}
