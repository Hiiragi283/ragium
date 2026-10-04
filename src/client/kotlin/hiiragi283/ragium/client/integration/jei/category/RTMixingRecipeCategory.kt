package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.api.recipe.RTMixingRecipe
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTMixingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTMixingRecipe>(
        guiHelper,
        RagiumJeiRecipeTypes.MIXING,
        18 * 9,
        18 * 1,
        RTMixingRecipe.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTMixingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.itemIngredients.head)
            .setSlotBackground(HTBackgroundType.INPUT)
        val secondInput: IRecipeSlotBuilder = builder
            .addInputSlot(getPosition(3), getPosition(0))
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        recipe.itemIngredients.getOrNull(1)?.let(secondInput::add)
        // outputs
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(8), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.content.mapLeft(itemOutput::add).mapRight(fluidOutput::add)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTMixingRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(4.25), getPosition(0))
        builder.addRecipePlus(getPosition(7))
    }
}
