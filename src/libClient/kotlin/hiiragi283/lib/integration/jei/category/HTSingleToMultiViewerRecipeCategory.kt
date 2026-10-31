package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.recipe.viewer.HTSingleToMultiViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.display.SlotDisplay

class HTSingleToMultiViewerRecipeCategory(recipeType: IRecipeType<HTSingleToMultiViewerRecipe>, recipeType1: RecipeType<*>, icon: IDrawable) :
    HTViewerRecipeCategory<HTSingleToMultiViewerRecipe>(recipeType, recipeType1, icon, 18 * 5, 18 * 2, HTSingleToMultiViewerRecipe.CODEC) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTSingleToMultiViewerRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0.5))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val outputs: List<IRecipeSlotBuilder> = List(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 2), getPosition(0 + index / 2))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
        recipe.results.forEachIndexed { index: Int, result: SlotDisplay ->
            outputs[index].add(result)
        }
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTSingleToMultiViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0.5))
    }
}
