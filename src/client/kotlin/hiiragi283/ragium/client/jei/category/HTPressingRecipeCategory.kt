package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTPressingRecipeCategory(guiHelper: IGuiHelper) :
    HTProcessingRecipeCategory<HTPressingRecipe>(guiHelper, RagiumJeiRecipeTypes.PRESSING) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTPressingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTPressingRecipe, focuses: IFocusGroup) {
        builder.setShapeless()
        // inputs
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .addItemIngredient(recipe.ingredients[0])
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(1), getPosition(1))
            .addItemIngredient(recipe.ingredients[1])
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(1), getPosition(2))
            .addItemIngredient(recipe.ingredients.getOrNull(2))
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5.5), getPosition(1))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }
}
