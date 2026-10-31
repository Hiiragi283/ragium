package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.addOptional
import hiiragi283.lib.recipe.viewer.HTItemOrFluidViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.world.item.crafting.RecipeType

class HTItemOrFluidViewerRecipeCategory(recipeType: IRecipeType<HTItemOrFluidViewerRecipe>, recipeType1: RecipeType<*>, icon: IDrawable) :
    HTViewerRecipeCategory<HTItemOrFluidViewerRecipe>(recipeType, recipeType1, icon, 18 * 8, 18 * 1, HTItemOrFluidViewerRecipe.CODEC) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemOrFluidViewerRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addOptional(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addOptional(recipe.itemIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addOptional(recipe.itemResult)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(7), getPosition(0))
            .addOptional(recipe.fluidResult)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemOrFluidViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
