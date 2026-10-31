package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.addOptional
import hiiragi283.lib.recipe.viewer.HTFluidSeparatingViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.world.item.crafting.RecipeType

class HTFluidSeparatingRecipeCategory(recipeType: IRecipeType<HTFluidSeparatingViewerRecipe>, recipeType1: RecipeType<*>, icon: IDrawable) :
    HTViewerRecipeCategory<HTFluidSeparatingViewerRecipe>(
        recipeType,
        recipeType1,
        icon,
        18 * 7,
        18 * 1,
        HTFluidSeparatingViewerRecipe.CODEC,
    ) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTFluidSeparatingViewerRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addOptional(recipe.itemResult)
            .setSlotBackground(HTBackgroundType.OUTPUT)

        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.fluidResults.first())
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .addOptional(recipe.fluidResults.getOrNull(1))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTFluidSeparatingViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
