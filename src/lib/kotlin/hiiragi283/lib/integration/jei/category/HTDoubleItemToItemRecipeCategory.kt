package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTDoubleItemToItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTDoubleItemToItemRecipe.Basic>) : HTHolderRecipeCategory<HTDoubleItemToItemRecipe.Basic>(guiHelper, recipeType, HTDoubleItemToItemRecipe.Basic.SIMPLE_CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTDoubleItemToItemRecipe.Basic, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.primary)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.secondary)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTDoubleItemToItemRecipe.Basic, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
