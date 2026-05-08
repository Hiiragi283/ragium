package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTChemicalReactingRecipeCategory(guiHelper: IGuiHelper) :
    HTDisplayRecipeCategory.Progress(guiHelper, RagiumRecipeViewerTypes.CHEMICAL_REACTING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        contents.inputFluid(0) {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .addFluidStacks(it.stacks)
                .setTankBackground(HTBackgroundType.INPUT, it.capacity)
        }
        contents.inputFluid(1) {
            builder
                .addInputSlot(getPosition(2), getPosition(0))
                .addFluidStacks(it.stacks)
                .setTankBackground(HTBackgroundType.EXTRA_INPUT, it.capacity)
        }
        // catalyst
        builder
            .addInputSlot(getPosition(3.5), getPosition(0))
            .addItemStacks(contents.catalyst(0))
            .setSlotBackground(HTBackgroundType.NONE)
        // outputs
        contents.outputFluid(0) {
            builder
                .addOutputSlot(getPosition(5), getPosition(0))
                .addFluidStack(it)
                .setTankBackground(HTBackgroundType.OUTPUT, it.amount)
        }
        contents.outputFluid(1) {
            builder
                .addOutputSlot(getPosition(7), getPosition(0))
                .addFluidStack(it)
                .setTankBackground(HTBackgroundType.EXTRA_OUTPUT, it.amount)
        }
        builder
            .addOutputSlot(getPosition(3.5), getPosition(2))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1), getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(1))
        builder.addRecipePlus(getPosition(6), getPosition(1))
    }
}
