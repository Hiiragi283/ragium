package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeType

class HTItemOrFluidRecipeCategory(
    recipeType: HTRecipeHolderType<HTItemAndFluidToRecipe.BasicItemAndFluid>,
    recipeType1: RecipeType<*>,
    icon: IDrawable
) : HTHolderRecipeCategory<HTItemAndFluidToRecipe.BasicItemAndFluid>(
    recipeType,
    recipeType1,
    icon,
    18 * 8,
    18 * 1,
    HTItemAndFluidToRecipe.BasicItemAndFluid.SIMPLE_CODEC
) {
    override fun setupRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: HTItemAndFluidToRecipe.BasicItemAndFluid,
        focuses: IFocusGroup
    ) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.itemIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(7), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.content.mapLeft(itemOutput::add).mapRight(fluidOutput::add)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTItemAndFluidToRecipe.BasicItemAndFluid,
        focuses: IFocusGroup
    ) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
