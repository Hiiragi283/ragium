package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemToRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemToItemAndFluidRecipeCategory(
    guiHelper: IGuiHelper,
    recipeType: HTHolderJeiRecipeType<HTItemToRecipe.BasicItemAndFluid>
) : HTHolderRecipeCategory<HTItemToRecipe.BasicItemAndFluid>(
    guiHelper,
    recipeType,
    18 * 6,
    18 * 1,
    HTItemToRecipe.BasicItemAndFluid.SIMPLE_CODEC
) {
    override fun setupRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: HTItemToRecipe.BasicItemAndFluid,
        focuses: IFocusGroup
    ) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.content.mapLeft(itemOutput::add).mapRight(fluidOutput::add)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTItemToRecipe.BasicItemAndFluid,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
