package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.addOptional
import hiiragi283.lib.integration.jei.category.HTViewerRecipeCategory
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.recipe.viewer.RTFluidReactingViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTFluidReactingRecipeCategory(guiHelper: IGuiHelper) :
    HTViewerRecipeCategory<RTFluidReactingViewerRecipe>(
        RagiumJeiRecipeTypes.FLUID_REACTING,
        RagiumTranslation.RECIPE_CATEGORY_FLUID_REACTING,
        guiHelper.createDrawableItemLike(RagiumBlocks.CHEMICAL_REACTOR),
        18 * 8,
        18 * 2,
        RTFluidReactingViewerRecipe.CODEC,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RTFluidReactingViewerRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addOptional(recipe.ingredients.firstOrNull())
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(0), getPosition(1))
            .addOptional(recipe.ingredients.getOrNull(1))
            .setSlotBackground(HTBackgroundType.INPUT)
        // catalyst
        builder
            .addInputSlot(getPosition(2), getPosition(0.5))
            .addOptional(recipe.catalyst)
            .setSlotBackground(HTBackgroundType.NONE)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(0.5))
            .addOptional(recipe.itemResult)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(7), getPosition(0))
            .addOptional(recipe.fluidResults.firstOrNull())
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(7), getPosition(1))
            .addOptional(recipe.fluidResults.getOrNull(1))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTFluidReactingViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1), getPosition(0.5))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0.5))
        builder.addRecipePlus(getPosition(6), getPosition(0.5))
    }
}
