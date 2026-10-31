package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.addOptional
import hiiragi283.lib.recipe.viewer.HTSingleToItemAndFluidViewerRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.world.item.crafting.RecipeType

class HTSingleToItemAndFluidViewerRecipeCategory(
    recipeType: IRecipeType<HTSingleToItemAndFluidViewerRecipe>,
    recipeType1: RecipeType<*>,
    icon: IDrawable,
) : HTViewerRecipeCategory<HTSingleToItemAndFluidViewerRecipe>(
    recipeType,
    recipeType1,
    icon,
    18 * 6,
    18 * 1,
    HTSingleToItemAndFluidViewerRecipe.CODEC,
) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTSingleToItemAndFluidViewerRecipe, focuses: IFocusGroup) {
        // input
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
            .addOptional(recipe.fluidResult)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTSingleToItemAndFluidViewerRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
