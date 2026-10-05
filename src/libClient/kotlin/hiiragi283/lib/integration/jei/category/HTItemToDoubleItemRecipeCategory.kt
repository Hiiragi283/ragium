package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeType

class HTItemToDoubleItemRecipeCategory(
    recipeType: HTRecipeHolderType<HTItemToDoubleItemRecipe.Basic>,
    recipeType1: RecipeType<*>,
    icon: IDrawable
) : HTHolderRecipeCategory<HTItemToDoubleItemRecipe.Basic>(
    recipeType,
    recipeType1,
    icon,
    18 * 6,
    18 * 1,
    HTItemToDoubleItemRecipe.Basic.SIMPLE_CODEC
) {
    override fun setupRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: HTItemToDoubleItemRecipe.Basic,
        focuses: IFocusGroup
    ) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(recipe.results.head)
            .setSlotBackground(HTBackgroundType.OUTPUT)

        val slot: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
        recipe.results.getOrNull(1)?.let(slot::add)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTItemToDoubleItemRecipe.Basic,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
