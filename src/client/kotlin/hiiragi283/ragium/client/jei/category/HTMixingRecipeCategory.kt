package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.viewer.HTViewerMixingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTMixingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<HTViewerMixingRecipe>(guiHelper, RagiumRecipeViewerTypes.MIXING, HTViewerMixingRecipe.CODEC.codec) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTViewerMixingRecipe, focuses: IFocusGroup) {
        val (
            itemIngredients: List<HTItemIngredient>,
            fluidIngredients: List<HTFluidIngredient>,
            itemResults: List<HTItemResult>,
            fluidResults: List<HTFluidResult>,
        ) = recipe
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemIngredient(itemIngredients.getOrNull(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .addItemIngredient(itemIngredients.getOrNull(1))
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addInputSlot(getPosition(0), getPosition(1))
            .addFluidIngredient(fluidIngredients.getOrNull(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(1), getPosition(1))
            .addFluidIngredient(fluidIngredients.getOrNull(1))
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .addItemResult(itemResults.getOrNull(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addItemResult(itemResults.getOrNull(1))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
        builder
            .addOutputSlot(getPosition(4), getPosition(1))
            .addFluidResult(fluidResults.getOrNull(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(5), getPosition(1))
            .addFluidResult(fluidResults.getOrNull(1))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTViewerMixingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(2.25), getPosition(0))
    }
}
