package hiiragi283.ragium.client.jei.category

import hiiragi283.core.client.jei.category.base.HTSingleMultiOutputRecipeCategory
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.helpers.IGuiHelper

class HTCuttingRecipeCategory(guiHelper: IGuiHelper) : HTSingleMultiOutputRecipeCategory(guiHelper, RagiumRecipeViewerTypes.CUTTING, 2) {
    override fun getOutputPos(index: Int): Pair<Int, Int> = getPosition(3) to getPosition(index)
}
