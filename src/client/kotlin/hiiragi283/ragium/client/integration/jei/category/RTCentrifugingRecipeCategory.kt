package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.RTCentrifugingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTCentrifugingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTCentrifugingRecipe.Basic>(
        RagiumJeiRecipeTypes.CENTRIFUGING,
        RagiumRecipeTypes.CENTRIFUGING,
        guiHelper.createDrawableItemLike(RagiumBlocks.MACHINE_CASING),
        18 * 7,
        18 * 2,
        RTCentrifugingRecipe.Basic.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTCentrifugingRecipe.Basic, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0.5))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val itemOutputs: List<IRecipeSlotBuilder> = List(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 2), getPosition(index / 2))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
        recipe.result.items.forEachIndexed { index: Int, result: HTItemResult ->
            itemOutputs[index].add(result)
        }
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(00.5))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.fluid.ifPresent(fluidOutput::add)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: RTCentrifugingRecipe.Basic,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0.5))
        builder.addRecipePlus(getPosition(5), getPosition(0.5))
    }
}
