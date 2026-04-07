package hiiragi283.ragium.client.jei.category

import hiiragi283.core.client.jei.category.base.HTSingleMultiOutputRecipeCategory
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import mezz.jei.api.helpers.IGuiHelper

class HTPlantingRecipeCategory(guiHelper: IGuiHelper) : HTSingleMultiOutputRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PLANTING, 4) {
    override fun getOutputPos(index: Int): Pair<Int, Int> = getPosition(3 + index % 2) to getPosition(0 + index / 2)
}
