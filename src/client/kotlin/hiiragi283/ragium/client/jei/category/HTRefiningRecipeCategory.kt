package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.item.createItemStack
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTRefiningRecipeCategory(guiHelper: IGuiHelper) :
    HTProcessingRecipeCategory<HTRefiningRecipe>(guiHelper, RagiumJeiRecipeTypes.REFINING) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTRefiningRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTRefiningRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(true, recipe.ingredient)
            .setTankBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addInputSlot(getPosition(1.5), getPosition(0.5))
            .addItemStack(createItemStack(RagiumItems.BLUE_PRINT, RagiumDataComponents.BLUE_PRINT_NUMBER, recipe.number))
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(1))
            .addItemResult(recipe.result.getLeft())
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(6.5), getPosition(0))
            .addFluidResult(true, recipe.result.getRight())
            .setTankBackground(HTBackgroundType.EXTRA_OUTPUT)
    }
}
