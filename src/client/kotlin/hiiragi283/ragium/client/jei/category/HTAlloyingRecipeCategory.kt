package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTAlloyingRecipeCategory(guiHelper: IGuiHelper) :
    HTProcessingRecipeCategory<HTAlloyingRecipe>(guiHelper, RagiumJeiRecipeTypes.ALLOYING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTAlloyingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addItemSlot(getPosition(0), getPosition(0.5), recipe.ingredients[0])
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addItemSlot(getPosition(1), getPosition(0.5), recipe.ingredients[1])
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addItemSlot(getPosition(2), getPosition(0.5), recipe.ingredients.getOrNull(2))
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addItemSlot(getPosition(5.5), getPosition(0.5), recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addItemSlot(getPosition(5.5), getPosition(2), recipe.extraResults.getOrNull(0))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTAlloyingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1))
        builder.addAnimatedRecipeFlame(recipe.time).setPosition(getPosition(1), getPosition(1.5))
    }
}
