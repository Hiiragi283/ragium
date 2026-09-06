package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTReactingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTReactingRecipe>(
        guiHelper,
        RagiumJeiRecipeTypes.REACTING,
        18 * 8,
        18 * 1,
        RTReactingRecipe.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTReactingRecipe, focuses: IFocusGroup) {
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
        recipe.fluidResult.let {
            builder
                .addOutputSlot(getPosition(5), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.OUTPUT, it.amount)
        }
        val itemOutput: IRecipeSlotBuilder = builder.addOutputSlot(
            getPosition(7),
            getPosition(0)
        ).setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.itemResult.ifPresent(itemOutput::add)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTReactingRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
