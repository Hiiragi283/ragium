package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 1種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemToItemRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<ItemStack>,
    HTProgressRecipe<SingleRecipeInput> {

    class Basic(
        val ingredient: HTItemIngredient,
        val result: HTItemResult,
        override val progressData: HTProgressData,
    ) : HTItemToItemRecipe,
        HTProgressRecipe.Simple<SingleRecipeInput> {
        override fun test(input: TypedInstance<Item>): Boolean = ingredient.test(input)

        override fun getMatchingStack(input: TypedInstance<Item>): TypedInstance<Item> = ingredient.getMatchingStack(input)

        override fun apply(input: ItemInstance): ItemStack = result.createStackOrEmpty()
    }
}
