package hiiragi283.ragium.api.integration.jei

import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup

/**
 * @see mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension
 */
interface HTItemOrFluidRecipeCategoryExtension<RECIPE : HTItemOrFluidRecipe> {
    fun <T : IIngredientAcceptor<T>> setInputFluid(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setInputItem(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutputItem(recipe: RECIPE, accessor: T) {}

    fun <T : IIngredientAcceptor<T>> setOutputFluid(recipe: RECIPE, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        inputFluid: IRecipeSlotDrawable,
        inputItem: IRecipeSlotDrawable,
        outputItem: IRecipeSlotDrawable,
        outputFluid: IRecipeSlotDrawable,
        focuses: IFocusGroup,
    ) {}
}
