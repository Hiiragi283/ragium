package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeType

class HTFluidSeparatingRecipeCategory(
    recipeType: HTRecipeHolderType<HTFluidSeparatingRecipe>,
    recipeType1: RecipeType<*>,
    icon: IDrawable
) : HTHolderRecipeCategory<HTFluidSeparatingRecipe>(
    recipeType,
    recipeType1,
    icon,
    18 * 7,
    18 * 1,
    HTFluidSeparatingRecipe.SIMPLE_CODEC
) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTFluidSeparatingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.item.ifPresent(itemOutput::add)

        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.result.fluids.head)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
        recipe.result.fluids.getOrNull(1)?.let(fluidOutput::add)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTFluidSeparatingRecipe,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
