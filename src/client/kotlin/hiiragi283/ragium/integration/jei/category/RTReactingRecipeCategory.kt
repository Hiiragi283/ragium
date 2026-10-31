package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.category.HTViewerRecipeCategory
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.recipe.viewer.RTReactingViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole

class RTReactingRecipeCategory(guiHelper: IGuiHelper) :
    HTViewerRecipeCategory<RTReactingViewerRecipe>(
        RagiumJeiRecipeTypes.REACTING,
        RagiumRecipeTypes.REACTING,
        guiHelper.createDrawableItemLike(RagiumBlocks.CHEMICAL_REACTOR),
        18 * 6,
        18 * 2,
        RTReactingViewerRecipe.CODEC,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RTReactingViewerRecipe, focuses: IFocusGroup) {
        // inputs
        val fluidInputs: List<IRecipeSlotBuilder> = List(2) { index: Int ->
            builder
                .addInputSlot(getPosition(0), getPosition(index))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        val itemInputs: List<IRecipeSlotBuilder> = List(2) { index: Int ->
            builder
                .addInputSlot(getPosition(2), getPosition(index))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        fluidInputs.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            recipe.fluidIngredients.getOrNull(index)?.let(builder::add)
        }
        itemInputs.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            recipe.itemIngredients.getOrNull(index)?.let(builder::add)
        }
        // catalyst
        builder
            .addSlot(RecipeIngredientRole.CRAFTING_STATION, getPosition(3.5), getPosition(0))
            .add(recipe.catalyst)
            .setSlotBackground(HTBackgroundType.NONE)
        // outputs
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(5), getPosition(1))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.itemResult.ifPresent(itemOutput::add)
        recipe.fluidResult.ifPresent(fluidOutput::add)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTReactingViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1), getPosition(0.5))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(1))
    }
}
