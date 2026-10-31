package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.category.HTViewerRecipeCategory
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.recipe.viewer.RTMixingViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTMixingRecipeCategory(guiHelper: IGuiHelper) :
    HTViewerRecipeCategory<RTMixingViewerRecipe>(
        RagiumJeiRecipeTypes.MIXING,
        RagiumRecipeTypes.MIXING,
        guiHelper.createDrawableItemLike(RagiumBlocks.MIXER),
        18 * 7,
        18 * 2,
        RTMixingViewerRecipe.CODEC,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RTMixingViewerRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0.5))
            .add(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        val itemInputs: List<IRecipeSlotBuilder> = List(4) { index: Int ->
            builder
                .addInputSlot(getPosition(2 + index % 2), getPosition(index / 2))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        itemInputs.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            recipe.itemIngredients.getOrNull(index)?.let(builder::add)
        }
        // outputs
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(1))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.itemResult.ifPresent(itemOutput::add)
        recipe.fluidResult.ifPresent(fluidOutput::add)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTMixingViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1), getPosition(0.5))
        builder.addRecipeArrow(recipe).setPosition(getPosition(4.25), getPosition(0.5))
    }
}
