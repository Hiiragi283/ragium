package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemToItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTItemToItemRecipe.Basic>) : HTHolderRecipeCategory<HTItemToItemRecipe.Basic>(guiHelper, recipeType, HTItemToItemRecipe.Basic.SIMPLE_CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemToItemRecipe.Basic, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemToItemRecipe.Basic, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
    }
}
