package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.recipe.viewer.HTDoubleToSingleViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.world.item.crafting.RecipeType

class HTDoubleToSingleViewerRecipeCategory(
    recipeType: IRecipeType<HTDoubleToSingleViewerRecipe>,
    recipeType1: RecipeType<*>,
    icon: IDrawable,
    val secondInputType: HTBackgroundType,
) : HTViewerRecipeCategory<HTDoubleToSingleViewerRecipe>(recipeType, recipeType1, icon, 18 * 6, 18 * 1, HTDoubleToSingleViewerRecipe.CODEC) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTDoubleToSingleViewerRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.primary)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.secondary)
            .setSlotBackground(secondInputType)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTDoubleToSingleViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
