package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.api.recipe.RTCentrifugingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTCentrifugingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTCentrifugingRecipe.Basic>(
        RagiumJeiRecipeTypes.CENTRIFUGING,
        RagiumRecipeTypes.CENTRIFUGING,
        guiHelper.createDrawableItemLike(RagiumBlocks.CENTRIFUGE),
        18 * 7,
        18 * 1,
        RTCentrifugingRecipe.Basic.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTCentrifugingRecipe.Basic, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(recipe.result.items.head)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.items.getOrNull(1)?.let(itemOutput::add)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.fluid.ifPresent(fluidOutput::add)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: RTCentrifugingRecipe.Basic,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(5))
    }
}
