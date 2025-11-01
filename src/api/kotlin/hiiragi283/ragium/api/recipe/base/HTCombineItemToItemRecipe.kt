package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTMultiItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

interface HTCombineItemToItemRecipe : HTMultiItemToObjRecipe {
    val ingredients: List<HTItemIngredient>
    val result: HTItemResult

    override fun test(input: HTMultiItemRecipeInput): Boolean = HTMultiItemToObjRecipe.hasMatchingSlots(ingredients, input.items)

    override fun assembleItem(input: HTMultiItemRecipeInput, registries: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, registries, result)

    override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.any(HTItemIngredient::hasNoMatchingStacks) || result.hasNoMatchingStack()
}
