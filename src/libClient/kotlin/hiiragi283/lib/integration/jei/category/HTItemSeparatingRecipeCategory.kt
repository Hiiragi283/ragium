package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.result.HTItemResult
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeType

class HTItemSeparatingRecipeCategory(
    recipeType: HTRecipeHolderType<HTItemSeparatingRecipe.Basic>,
    recipeType1: RecipeType<*>,
    icon: IDrawable
) : HTHolderRecipeCategory<HTItemSeparatingRecipe.Basic>(
    recipeType,
    recipeType1,
    icon,
    18 * 7,
    18 * 2,
    HTItemSeparatingRecipe.Basic.SIMPLE_CODEC
) {
    override fun setupRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: HTItemSeparatingRecipe.Basic,
        focuses: IFocusGroup
    ) {
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
        recipe.result.items.forEachIndexed { index: Int, result: HTItemResult ->
            outputs[index].add(result)
        }
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(0.5))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
        recipe.result.fluid.ifPresent(fluidOutput::add)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTItemSeparatingRecipe.Basic,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0.5))
        builder.addRecipePlus(getPosition(5), getPosition(0.5))
    }
}
