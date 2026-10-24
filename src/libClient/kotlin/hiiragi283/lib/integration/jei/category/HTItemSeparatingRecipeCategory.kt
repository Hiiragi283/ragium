package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.recipe.viewer.HTItemSeparatingViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.display.SlotDisplay

class HTItemSeparatingRecipeCategory(recipeType: IRecipeType<HTItemSeparatingViewerRecipe>, recipeType1: RecipeType<*>, icon: IDrawable) :
    HTViewerRecipeCategory<HTItemSeparatingViewerRecipe>(
        recipeType,
        recipeType1,
        icon,
        18 * 7,
        18 * 2,
        HTItemSeparatingViewerRecipe.CODEC,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemSeparatingViewerRecipe, focuses: IFocusGroup) {
        // inputs
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
        recipe.itemResults.forEachIndexed { index: Int, result: SlotDisplay ->
            outputs[index].add(result)
        }
        builder
            .addOutputSlot(getPosition(6), getPosition(0.5))
            .add(recipe.fluidResult)
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemSeparatingViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0.5))
        builder.addRecipePlus(getPosition(5), getPosition(0.5))
    }
}
