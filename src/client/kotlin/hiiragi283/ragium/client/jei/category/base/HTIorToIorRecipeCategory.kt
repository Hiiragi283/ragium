package hiiragi283.ragium.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.HTJeiHolderRecipeType
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

abstract class HTIorToIorRecipeCategory<RECIPE : HTProcessingRecipe<HTItemAndFluidRecipeInput>>(
    guiHelper: IGuiHelper,
    recipeType: HTJeiHolderRecipeType<RECIPE>,
) : HTProcessingRecipeCategory<RECIPE>(guiHelper, recipeType) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // inputs
        val (itemIn: HTItemIngredient?, fluidIn: HTFluidIngredient?) = getIngredients(recipe).toPair()
        builder
            .addFluidSlot(getPosition(0), getPosition(0), true, fluidIn)
            .setTankBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addItemSlot(getPosition(1.5), getPosition(0.5), itemIn)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val (itemOut: HTItemResult?, fluidOut: HTFluidResult?) = getResults(recipe).toPair()
        builder
            .addFluidSlot(getPosition(6.5), getPosition(0), true, fluidOut)
            .setTankBackground(HTBackgroundType.EXTRA_OUTPUT)
        builder
            .addItemSlot(getPosition(5), getPosition(1), itemOut)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    protected abstract fun getIngredients(recipe: RECIPE): Ior<HTItemIngredient, HTFluidIngredient>

    protected abstract fun getResults(recipe: RECIPE): Ior<HTItemResult, HTFluidResult>
}
