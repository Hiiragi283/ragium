package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
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
            .addItemSlot(getPosition(1), getPosition(0), recipe.ingredients[0])
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addItemSlot(getPosition(1), getPosition(1), recipe.ingredients[1])
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addItemSlot(getPosition(1), getPosition(2), recipe.ingredients.getOrNull(2))
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addItemSlot(getPosition(5.5), getPosition(1), recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }
}
