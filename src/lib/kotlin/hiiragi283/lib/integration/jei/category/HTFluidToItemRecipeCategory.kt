package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTFluidToItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTFluidToRecipe.BasicItem>) :
    HTHolderRecipeCategory<HTFluidToRecipe.BasicItem>(
        guiHelper,
        recipeType,
        18 * 4,
        18 * 1,
        HTFluidToRecipe.BasicItem.SIMPLE_CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTFluidToRecipe.BasicItem, focuses: IFocusGroup) {
        // input
        recipe.ingredient.let {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.INPUT, it.amount)
        }
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTFluidToRecipe.BasicItem,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
    }
}
