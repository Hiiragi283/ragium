package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidStack
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.integration.jei.setFluidSlotRenderer
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTMeltingRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Progress(guiHelper, RagiumRecipeViewerTypes.MELTING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addFluidStack(contents.outputFluid(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
            .setFluidSlotRenderer()
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
    }
}
