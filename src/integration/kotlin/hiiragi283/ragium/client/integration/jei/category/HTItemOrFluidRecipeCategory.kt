package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemOrFluidRecipeCategory(guiHelper: IGuiHelper, recipeType: HTRecipeViewerType<HTProgressRecipeDisplay>) :
    HTDisplayRecipeCategory.Progress(guiHelper, recipeType) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        contents.inputFluid(0) {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .addFluidStacks(it.stacks)
                .setSlotBackground(HTBackgroundType.EXTRA_INPUT, it.capacity)
        }
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        contents.outputFluid(0) {
            builder
                .addOutputSlot(getPosition(7), getPosition(0))
                .addFluidStack(it)
                .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT, it.amount)
        }
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
