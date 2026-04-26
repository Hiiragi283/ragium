package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.recipe.viewer.display.HTProcessingRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTAlloyingRecipeCategory(guiHelper: IGuiHelper) :
    HTDisplayRecipeCategory.Processing(guiHelper, RagiumRecipeViewerTypes.ALLOYING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addItemStacks(contents.inputItem(1))
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addInputSlot(getPosition(3), getPosition(0))
            .addItemStacks(contents.inputItem(2))
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        // output
        builder
            .addInputSlot(getPosition(6), getPosition(0))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProcessingRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(4.25), getPosition(0))
    }
}
