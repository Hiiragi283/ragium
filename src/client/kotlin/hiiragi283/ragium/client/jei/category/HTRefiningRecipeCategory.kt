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

class HTRefiningRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Progress(guiHelper, RagiumRecipeViewerTypes.REFINING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidStacks(contents.inputFluid(0))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.INPUT)
        // catalyst
        builder
            .addInputSlot(getPosition(1.5), getPosition(0))
            .addItemStacks(contents.catalyst(0))
            .setSlotBackground(HTBackgroundType.NONE)
        // outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addFluidStack(contents.outputFluid(0))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addFluidStack(contents.outputFluid(1))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.EXTRA_OUTPUT)
        builder
            .addOutputSlot(getPosition(1.5), getPosition(2))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(1))
        builder.addRecipePlus(getPosition(4), getPosition(1))
    }
}
