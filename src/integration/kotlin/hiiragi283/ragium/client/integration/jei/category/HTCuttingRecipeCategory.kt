package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.client.integration.jei.category.base.HTItemToMultiItemRecipeCategory
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTCuttingRecipeCategory(guiHelper: IGuiHelper) : HTItemToMultiItemRecipeCategory(guiHelper, RagiumRecipeViewerTypes.CUTTING) {
    override fun setupOutputs(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(3), getPosition(1))
            .addChancedItem(contents.outputItem(1))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }
}
