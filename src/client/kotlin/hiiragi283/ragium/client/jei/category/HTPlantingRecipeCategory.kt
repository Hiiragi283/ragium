package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.client.jei.category.base.HTDoubleMultiOutputRecipeCategory
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTPlantingRecipeCategory(guiHelper: IGuiHelper) : HTDoubleMultiOutputRecipeCategory(guiHelper, RagiumRecipeViewerTypes.PLANTING) {
    override fun setupOutputs(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        repeat(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 2), getPosition(0.5 + index / 2))
                .addChancedItem(contents.outputItem(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }
}
