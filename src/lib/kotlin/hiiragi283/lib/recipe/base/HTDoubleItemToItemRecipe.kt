package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 2種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTDoubleItemToItemRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<ItemStack>,
    HTProgressRecipe<RecipeInput> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic(
        val primary: HTItemIngredient,
        val secondary: HTItemIngredient,
        val result: HTItemResult,
        override val progressData: HTProgressData,
    ) : HTDoubleItemToItemRecipe,
        HTProgressRecipe.Simple<RecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: (HTItemIngredient, HTItemIngredient, HTItemResult, HTProgressData) -> RECIPE): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.PRIMARY).forGetter(Basic::primary),
                    HTItemIngredient.CODEC.fieldOf(HTConstants.SECONDARY).forGetter(Basic::secondary),
                    HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                    HTProgressData.CODEC.forGetter(Basic::progressData),
                ).apply(instance, factory)
            }
        }

        override fun test(first: TypedInstance<Item>, second: TypedInstance<Item>): Boolean = primary.test(first) && secondary.test(second)

        override fun getRequiredAmount(first: TypedInstance<Item>, second: TypedInstance<Item>): Pair<Int, Int> = primary.getRequiredAmount(first) to secondary.getRequiredAmount(second)

        override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.createOrEmpty()
    }
}
