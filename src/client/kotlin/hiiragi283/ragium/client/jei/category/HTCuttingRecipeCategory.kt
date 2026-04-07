package hiiragi283.ragium.client.jei.category

import hiiragi283.core.client.jei.category.base.HTSingleMultiOutputRecipeCategory
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import mezz.jei.api.helpers.IGuiHelper

class HTCuttingRecipeCategory(guiHelper: IGuiHelper) : HTSingleMultiOutputRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING, 2) {
    override fun getOutputPos(index: Int): Pair<Int, Int> = getPosition(3) to getPosition(index)
}
