package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.client.jei.category.base.HTSingleMultiOutputRecipeCategory
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTCuttingRecipeCategory(guiHelper: IGuiHelper) :
    HTSingleMultiOutputRecipeCategory<HTCuttingRecipe>(guiHelper, RagiumRecipeViewerTypes.CUTTING, RagiumRecipeSerializers.CUTTING) {
    override fun setupOutputs(builder: IRecipeLayoutBuilder, recipe: HTCuttingRecipe, focuses: IFocusGroup) {
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addItemResult(recipe.results.getOrNull(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(3), getPosition(1))
            .addItemResult(recipe.results.getOrNull(1))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }
}
