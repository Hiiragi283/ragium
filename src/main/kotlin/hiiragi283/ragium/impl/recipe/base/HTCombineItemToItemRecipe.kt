package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTMultiInputsToObjRecipe
import hiiragi283.ragium.api.recipe.multi.HTMultiItemsToItemRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

/**
 * [HTMultiItemsToItemRecipe]の抽象クラス
 */
abstract class HTCombineItemToItemRecipe(final override val ingredients: List<HTItemIngredient>, val result: HTItemResult) :
    HTMultiItemsToItemRecipe {
    final override fun test(input: HTMultiRecipeInput): Boolean = HTMultiInputsToObjRecipe.hasMatchingSlots(ingredients, input.items)

    final override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, result)

    final override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.any(HTItemIngredient::hasNoMatchingStacks) || result.hasNoMatchingStack()
}
