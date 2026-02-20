package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTMixingRecipeCategory(guiHelper: IGuiHelper) :
    HTProcessingRecipeCategory<HTMixingRecipe>(guiHelper, RagiumJeiRecipeTypes.MIXING) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTMixingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTMixingRecipe, focuses: IFocusGroup) {
        builder.setShapeless()
        // inputs
        val (itemIn: List<HTItemIngredient>?, fluidIn: List<HTFluidIngredient>?) = recipe.ingredients.toPair()
        for (i: Int in (0 until HTMixingRecipe.MAX_ITEM_INPUT)) {
            builder
                .addInputSlot(getPosition(1 + i % 2), getPosition(i / 2))
                .addItemIngredient(itemIn?.getOrNull(i))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        for (i: Int in (0 until HTMixingRecipe.MAX_FLUID_INPUT)) {
            builder
                .addInputSlot(getPosition(1 + i % 2), getPosition(2))
                .addFluidIngredient(false, fluidIn?.getOrNull(i))
                .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        }
        // outputs
        val (itemOut: List<HTItemResult>?, fluidOut: List<HTFluidResult>?) = recipe.results.toPair()
        builder
            .addOutputSlot(getPosition(5.5), getPosition(0.5))
            .addItemResult(itemOut?.firstOrNull())
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(5.5), getPosition(2))
            .addFluidResult(false, fluidOut?.getOrNull(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(6.5), getPosition(2))
            .addFluidResult(false, fluidOut?.getOrNull(1))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }
}
