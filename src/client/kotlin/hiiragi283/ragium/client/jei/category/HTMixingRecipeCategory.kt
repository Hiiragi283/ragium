package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.integration.jei.setTankRenderer
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.config.RagiumConfig
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTMixingRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Progress(guiHelper, RagiumRecipeViewerTypes.MIXING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidStacks(contents.inputFluid(0))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0.5))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(1.5))
            .addItemStacks(contents.inputItem(1))
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(1))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(7), getPosition(0))
            .addFluidStack(contents.outputFluid(0))
            .setTankBackground(HTBackgroundType.EXTRA_OUTPUT)
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1), getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(1))
        builder.addRecipePlus(getPosition(6), getPosition(1))
    }
}
