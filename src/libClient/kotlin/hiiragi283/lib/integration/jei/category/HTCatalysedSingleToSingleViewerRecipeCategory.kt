package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.recipe.viewer.HTCatalysedSingleToSingleViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.world.item.crafting.RecipeType

class HTCatalysedSingleToSingleViewerRecipeCategory(
    recipeType: IRecipeType<HTCatalysedSingleToSingleViewerRecipe>,
    recipeType1: RecipeType<*>,
    icon: IDrawable,
) : HTViewerRecipeCategory<HTCatalysedSingleToSingleViewerRecipe>(
    recipeType,
    recipeType1,
    icon,
    18 * 6,
    18 * 1,
    HTCatalysedSingleToSingleViewerRecipe.CODEC,
) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTCatalysedSingleToSingleViewerRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // catalyst
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.catalyst)
            .setSlotBackground(HTBackgroundType.NONE)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTCatalysedSingleToSingleViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
