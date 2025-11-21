package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * [HTSingleItemRecipe]の抽象クラス
 */
abstract class HTBasicSingleItemRecipe(val ingredient: HTItemIngredient, result: HTItemResult) :
    HTBasicSingleOutputRecipe<SingleRecipeInput>(result),
    HTSingleItemRecipe {
    override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack()
}
