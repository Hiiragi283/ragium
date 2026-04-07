package hiiragi283.ragium.client.jei.extension

import hiiragi283.ragium.api.recipe.base.HTItemFluidMultiOutputRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.recipe.IFocusGroup
import java.util.function.IntFunction

interface HTItemFluidMultiOutputRecipeCategoryExtension<RECIPE : HTItemFluidMultiOutputRecipe> {
    fun <T : IIngredientAcceptor<T>> setItemInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setFluidInput(recipe: RECIPE, accessor: T)

    fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, index: Int, accessor: T) {}

    fun onDisplayedIngredientsUpdate(
        recipe: RECIPE,
        itemSlot: IRecipeSlotDrawable,
        fluidSlot: IRecipeSlotDrawable,
        outputSlots: IntFunction<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {}
}
