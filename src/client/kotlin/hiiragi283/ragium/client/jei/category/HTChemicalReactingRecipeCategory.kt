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

class HTChemicalReactingRecipeCategory(guiHelper: IGuiHelper) :
    HTDisplayRecipeCategory.Progress(guiHelper, RagiumRecipeViewerTypes.CHEMICAL_REACTING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidStacks(contents.inputFluid(0))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addFluidStacks(contents.inputFluid(1))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.EXTRA_INPUT)
        // catalyst
        builder
            .addInputSlot(getPosition(3.5), getPosition(0))
            .addItemStacks(contents.catalyst(0))
            .setSlotBackground(HTBackgroundType.NONE)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addFluidStack(contents.outputFluid(0))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(7), getPosition(0))
            .addFluidStack(contents.outputFluid(1))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.EXTRA_OUTPUT)
        builder
            .addOutputSlot(getPosition(3.5), getPosition(2))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1), getPosition(1))
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(1))
        builder.addRecipePlus(getPosition(6), getPosition(1))
    }
}
