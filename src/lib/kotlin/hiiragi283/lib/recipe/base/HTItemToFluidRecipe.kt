package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 1種類のアイテムから1種類の液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemToFluidRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<FluidStack>,
    HTProgressRecipe<SingleRecipeInput> {

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Basic(val ingredient: HTItemIngredient, val result: HTFluidResult, override val progressData: HTProgressData) :
        HTItemToFluidRecipe,
        HTProgressRecipe.Simple<SingleRecipeInput> {
        override fun test(input: TypedInstance<Item>): Boolean = ingredient.test(input)

        override fun getRequiredAmount(input: TypedInstance<Item>): Int = ingredient.getRequiredAmount(input)

        override fun apply(input: ItemInstance): FluidStack = result.create()
    }
}
