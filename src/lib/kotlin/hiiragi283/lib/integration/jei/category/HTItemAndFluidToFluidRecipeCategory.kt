package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.world.item.crafting.Ingredient

class HTItemAndFluidToFluidRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTItemAndFluidToRecipe.BasicFluid>) : HTHolderRecipeCategory<HTItemAndFluidToRecipe.BasicFluid>(guiHelper, recipeType, 18 * 6, 18 * 1, HTItemAndFluidToRecipe.BasicFluid.SIMPLE_CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemAndFluidToRecipe.BasicFluid, focuses: IFocusGroup) {
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
        recipe.result.let {
            builder
                .addOutputSlot(getPosition(5), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.OUTPUT, it.amount)
        }
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemAndFluidToRecipe.BasicFluid, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
