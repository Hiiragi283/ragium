package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.category.HTViewerRecipeCategory
import hiiragi283.lib.recipe.viewer.HTMultiToSingleViewerRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTAssemblingRecipeCategory(guiHelper: IGuiHelper) :
    HTViewerRecipeCategory<HTMultiToSingleViewerRecipe>(
        RagiumJeiRecipeTypes.ASSEMBLING,
        RagiumRecipeTypes.ASSEMBLING,
        guiHelper.createDrawableItemLike(RagiumBlocks.ASSEMBLER),
        18 * 5,
        18 * 2,
        HTMultiToSingleViewerRecipe.CODEC,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTMultiToSingleViewerRecipe, focuses: IFocusGroup) {
        // inputs
        val inputSlots: List<IRecipeSlotBuilder> = List(4) { index: Int ->
            builder
                .addInputSlot(getPosition(index % 2), getPosition(index / 2))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        inputSlots.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            recipe.ingredients.getOrNull(index)?.let(builder::add)
        }
        builder
            .addOutputSlot(getPosition(4), getPosition(0.5))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTMultiToSingleViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(2.25), getPosition(0.5))
    }
}
