package hiiragi283.ragium.api.integration.jei

import hiiragi283.ragium.api.recipe.HTItemToChancedRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTItemToChancedRecipeCategoryExtension<RECIPE : HTItemToChancedRecipe> {
    fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {}

    fun <T : IIngredientAcceptor<T>> setExtraOutput(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        inputSlot: IRecipeSlotDrawable,
        outputSlot: IRecipeSlotDrawable,
        extraOutputSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
