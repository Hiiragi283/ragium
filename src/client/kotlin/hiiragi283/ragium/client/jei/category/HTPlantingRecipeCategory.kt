package hiiragi283.ragium.client.jei.category

import hiiragi283.core.client.jei.category.base.HTDoubleMultiOutputRecipeCategory
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.helpers.IGuiHelper

class HTPlantingRecipeCategory(guiHelper: IGuiHelper) : HTDoubleMultiOutputRecipeCategory(guiHelper, RagiumRecipeViewerTypes.PLANTING, 4) {
    override fun getOutputPos(index: Int): Pair<Int, Int> = getPosition(3.5 + index % 2) to getPosition(0.5 + index / 2)
}
