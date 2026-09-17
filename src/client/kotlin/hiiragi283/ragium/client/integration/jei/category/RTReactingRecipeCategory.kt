package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTReactingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTReactingRecipe.Basic>(
        guiHelper,
        RagiumJeiRecipeTypes.REACTING,
        18 * 8,
        18 * 1,
        RTReactingRecipe.Basic.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTReactingRecipe.Basic, focuses: IFocusGroup) {
        // inputs
        recipe.primary.let {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.INPUT, it.amount)
        }
        recipe.secondary.let {
            builder
                .addInputSlot(getPosition(2), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.EXTRA_INPUT, it.amount)
        }
        // outputs
        val (itemResult: HTItemResult?, fluidResult: HTFluidResult?) = recipe.results.toPair()
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(7), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        fluidResult?.let { fluidOutput.add(it).setFluidSlot(it.amount) }
        itemResult?.let(itemOutput::add)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: RTReactingRecipe.Basic,
        focuses: IFocusGroup
    ) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
