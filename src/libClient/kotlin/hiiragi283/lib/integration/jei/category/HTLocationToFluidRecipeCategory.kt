package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTLocationToRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.world.item.crafting.RecipeType

class HTLocationToFluidRecipeCategory(
    recipeType: HTRecipeHolderType<HTLocationToRecipe.BasicFluid>,
    recipeType1: RecipeType<*>,
    icon: IDrawable
) : HTHolderRecipeCategory<HTLocationToRecipe.BasicFluid>(
    recipeType,
    recipeType1,
    icon,
    18 * 4,
    18 * 1,
    HTLocationToRecipe.BasicFluid.SIMPLE_CODEC
) {
    override fun setupRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: HTLocationToRecipe.BasicFluid,
        focuses: IFocusGroup
    ) {
        // biome condition
        builder
            .addSlot(RecipeIngredientRole.RENDER_ONLY, getPosition(0), getPosition(0))
            .add(recipe.condition.display)
        // output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTLocationToRecipe.BasicFluid,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
    }
}
