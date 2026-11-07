package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.addOptional
import hiiragi283.lib.integration.jei.category.HTViewerRecipeCategory
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.recipe.viewer.RTItemReactingViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTItemReactingRecipeCategory(guiHelper: IGuiHelper) :
    HTViewerRecipeCategory<RTItemReactingViewerRecipe>(
        RagiumJeiRecipeTypes.ITEM_REACTING,
        RagiumRecipeTypes.ITEM_REACTING,
        guiHelper.createDrawableItemLike(RagiumBlocks.CHEMICAL_REACTOR),
        18 * 8,
        18 * 2,
        RTItemReactingViewerRecipe.CODEC,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RTItemReactingViewerRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(1))
            .addOptional(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(1))
            .addOptional(recipe.itemIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // catalyst
        builder
            .addInputSlot(getPosition(3.5), getPosition(0))
            .addOptional(recipe.catalyst)
            .setSlotBackground(HTBackgroundType.NONE)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(1))
            .addOptional(recipe.itemResult)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(7), getPosition(1))
            .addOptional(recipe.fluidResult)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTItemReactingViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1), getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(1))
        builder.addRecipePlus(getPosition(6), getPosition(1))
    }
}
