package hiiragi283.ragium.api.integration.jei

import hiiragi283.ragium.api.recipe.HTEnchantingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

interface HTEnchantingRecipeCategoryExtension<RECIPE : HTEnchantingRecipe> {
    fun <T : IIngredientAcceptor<T>> setExpInput(recipe: RECIPE, accessor: T) {}

    fun <T : IIngredientAcceptor<T>> setBookInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setItemInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        expInputSlot: IRecipeSlotDrawable,
        bookInputSlot: IRecipeSlotDrawable,
        itemInputSlot: IRecipeSlotDrawable,
        outputSlot: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
