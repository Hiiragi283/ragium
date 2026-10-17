package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import java.util.Optional

class RTElectrolyzingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTElectrolyzingRecipe>(
        RagiumJeiRecipeTypes.ELECTROLYZING,
        RagiumRecipeTypes.ELECTROLYZING,
        guiHelper.createDrawableItemLike(RagiumBlocks.ELECTROLYZER),
        18 * 7,
        18 * 1,
        RTElectrolyzingRecipe.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTElectrolyzingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val (right: HTFluidResult, left: HTFluidResult, item: Optional<HTItemResult>) = recipe.result
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(right)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .add(left)
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        item.ifPresent(itemOutput::add)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTElectrolyzingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(5))
    }
}
