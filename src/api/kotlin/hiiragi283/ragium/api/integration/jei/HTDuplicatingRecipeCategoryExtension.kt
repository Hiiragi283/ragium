package hiiragi283.ragium.api.integration.jei

import hiiragi283.ragium.api.recipe.HTDuplicatingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTDuplicatingRecipeCategoryExtension<RECIPE : HTDuplicatingRecipe> {
    fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setRequiredMatter(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        inputSlot: IRecipeSlotDrawable,
        matterSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
