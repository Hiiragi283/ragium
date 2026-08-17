package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.world.item.crafting.Ingredient

class HTItemAndFluidToItemRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTItemAndFluidToItemRecipe.Basic>) : HTHolderRecipeCategory<HTItemAndFluidToItemRecipe.Basic>(guiHelper, recipeType, 18 * 6, 18 * 1, HTItemAndFluidToItemRecipe.Basic.SIMPLE_CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemAndFluidToItemRecipe.Basic, focuses: IFocusGroup) {
        // input
        recipe.fluidIngredient.let {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.INPUT, it.amount)
        }
        recipe.itemIngredient.fold(
            { catalyst: Ingredient ->
                builder
                    .addSlot(RecipeIngredientRole.CRAFTING_STATION, getPosition(2), getPosition(0))
                    .add(catalyst)
                    .setSlotBackground(HTBackgroundType.NONE)
            },
            { ingredient: HTItemIngredient ->
                builder
                    .addInputSlot(getPosition(2), getPosition(0))
                    .add(ingredient)
                    .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
            },
        )
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemAndFluidToItemRecipe.Basic, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
