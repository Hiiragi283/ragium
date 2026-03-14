package hiiragi283.ragium.api.integration.jei

import hiiragi283.ragium.api.recipe.HTTankInteractingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTTankInteractingRecipeCategoryExtension<RECIPE : HTTankInteractingRecipe> {
    fun <T : IIngredientAcceptor<T>> setEmptyContainer(recipe: RECIPE, accessor: T) {}

    fun <T : IIngredientAcceptor<T>> setFilledContainer(recipe: RECIPE, accessor: T) {}

    fun <T : IIngredientAcceptor<T>> setFluid(recipe: RECIPE, accessor: T) {}

    fun getTankCapacity(recipe: RECIPE): Long

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        emptySlot: IRecipeSlotDrawable,
        filledSlot: IRecipeSlotDrawable,
        fluidSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
