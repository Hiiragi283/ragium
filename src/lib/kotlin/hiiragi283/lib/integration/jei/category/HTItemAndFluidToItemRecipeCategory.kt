package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.ingredient.HTCatalystOrIngredient
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole

class HTItemAndFluidToItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTItemAndFluidToRecipe.BasicItem>) : HTHolderRecipeCategory<HTItemAndFluidToRecipe.BasicItem>(guiHelper, recipeType, 18 * 6, 18 * 1, HTItemAndFluidToRecipe.BasicItem.SIMPLE_CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemAndFluidToRecipe.BasicItem, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        val ingredient: HTCatalystOrIngredient = recipe.itemIngredient
        if (ingredient.isCatalyst) {
            builder
                .addSlot(RecipeIngredientRole.CRAFTING_STATION, getPosition(2), getPosition(0))
                .add(ingredient)
                .setSlotBackground(HTBackgroundType.NONE)
        } else {
            builder
                .addInputSlot(getPosition(2), getPosition(0))
                .add(ingredient)
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemAndFluidToRecipe.BasicItem, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
