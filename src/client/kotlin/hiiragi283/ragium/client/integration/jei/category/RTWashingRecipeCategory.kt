package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.api.recipe.RTWashingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTWashingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTWashingRecipe>(
        RagiumJeiRecipeTypes.WASHING,
        RagiumRecipeTypes.WASHING,
        guiHelper.createDrawableItemLike(RagiumBlocks.MACHINE_CASING),
        18 * 8,
        18 * 1,
        RTWashingRecipe.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTWashingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.itemIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.results.head)
            .setSlotBackground(HTBackgroundType.OUTPUT)

        val slot: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(7), getPosition(0))
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
        recipe.results.getOrNull(1)?.let(slot::add)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTWashingRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
