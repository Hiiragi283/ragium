package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.category.HTLookupRecipeCategory
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.common.recipe.RagiumDuplicatingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RagiumDuplicatingRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory<RagiumDuplicatingRecipe>(guiHelper, RagiumJeiRecipeTypes.DUPLICATING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RagiumDuplicatingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.NONE)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addFluidIngredient(recipe.defaultFluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: RagiumDuplicatingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }

    override fun onDisplayedIngredientsUpdate(
        recipe: HTRecipeHolder<RagiumDuplicatingRecipe>,
        recipeSlots: List<IRecipeSlotDrawable>,
        focuses: IFocusGroup,
    ) {
        recipeSlots[0].displayedItemStack.map(recipeSlots[2].createDisplayOverrides()::addItemStack)
    }
}
