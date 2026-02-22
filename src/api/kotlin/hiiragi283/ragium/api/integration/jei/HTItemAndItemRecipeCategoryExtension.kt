package hiiragi283.ragium.api.integration.jei

import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTItemAndItemRecipeCategoryExtension<RECIPE : HTItemAndItemRecipe> {
    fun <T : IIngredientAcceptor<T>> setFirstInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setSecondInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        firstInputSlot: IRecipeSlotDrawable,
        secondInputSlot: IRecipeSlotDrawable,
        outputSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
