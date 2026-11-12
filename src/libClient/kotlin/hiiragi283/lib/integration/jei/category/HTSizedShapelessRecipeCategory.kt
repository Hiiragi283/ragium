package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.recipe.viewer.HTMultiToSingleViewerRecipe
import hiiragi283.lib.text.HTHasText
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType

class HTSizedShapelessRecipeCategory(recipeType: IRecipeType<HTMultiToSingleViewerRecipe>, title: HTHasText, icon: IDrawable) :
    HTViewerRecipeCategory<HTMultiToSingleViewerRecipe>(
        recipeType,
        title,
        icon,
        18 * 6,
        18 * 1,
        HTMultiToSingleViewerRecipe.CODEC,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTMultiToSingleViewerRecipe, focuses: IFocusGroup) {
        // inputs
        val inputSlots: List<IRecipeSlotBuilder> = List(3) { index: Int ->
            builder
                .addInputSlot(getPosition(index), getPosition(0))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        inputSlots.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            recipe.ingredients.getOrNull(index)?.let(builder::add)
        }
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTMultiToSingleViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
