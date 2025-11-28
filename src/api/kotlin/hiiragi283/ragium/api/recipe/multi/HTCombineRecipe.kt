package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack

/**
 * 2種類のアイテムと単一の液体から，単一のアイテムを生産するレシピ
 */
interface HTCombineRecipe :
    HTRecipe<HTMultiRecipeInput>,
    HTFluidIngredient.AmountGetter {
    fun getLeftRequiredCount(stack: ImmutableItemStack): Int

    fun getRightRequiredCount(stack: ImmutableItemStack): Int
}
