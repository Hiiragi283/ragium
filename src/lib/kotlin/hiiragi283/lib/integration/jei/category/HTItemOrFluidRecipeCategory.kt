package hiiragi283.lib.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemOrFluidRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemOrFluidRecipeCategory(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTItemOrFluidRecipe.Basic>) : HTHolderRecipeCategory<HTItemOrFluidRecipe.Basic>(guiHelper, recipeType, 18 * 8, 18 * 1, HTItemOrFluidRecipe.Basic.SIMPLE_CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTItemOrFluidRecipe.Basic, focuses: IFocusGroup) {
        // inputs
        val fluidInput: IRecipeSlotBuilder = builder.addInputSlot(getPosition(0), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT)
        val itemInput: IRecipeSlotBuilder = builder.addInputSlot(getPosition(2), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT)
        recipe.ingredient
            .mapRight { fluidInput.add(it).setSlotBackground(HTBackgroundType.INPUT, it.amount) }
            .mapLeft { itemInput.add(it) }
        // outputs
        val itemOutput: IRecipeSlotBuilder = builder.addOutputSlot(getPosition(5), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder.addOutputSlot(getPosition(7), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result
            .mapRight { fluidOutput.add(it).setSlotBackground(HTBackgroundType.OUTPUT, it.amount) }
            .mapLeft { itemOutput.add(it) }
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTItemOrFluidRecipe.Basic, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
