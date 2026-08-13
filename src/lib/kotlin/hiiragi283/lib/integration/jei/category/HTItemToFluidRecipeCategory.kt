package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemToFluidRecipeCategory(guiHelper: IGuiHelper, recipeType: HTJeiRecipeType<HTRecipeHolder<HTItemToFluidRecipe.Basic>>) : HTHolderRecipeCategory<HTItemToFluidRecipe.Basic>(guiHelper, recipeType, HTItemToFluidRecipe.Basic.SIMPLE_CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemToFluidRecipe.Basic, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        recipe.result.create().let {
            builder
                .addOutputSlot(getPosition(3), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.OUTPUT, it.amount)
        }
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemToFluidRecipe.Basic, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
    }
}
