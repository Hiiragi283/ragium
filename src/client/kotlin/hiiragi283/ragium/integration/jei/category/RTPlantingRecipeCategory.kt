package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.api.recipe.RTPlantingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTPlantingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTPlantingRecipe>(
        RagiumJeiRecipeTypes.PLANTING,
        RagiumRecipeTypes.PLANTING,
        guiHelper.createDrawableItemLike(RagiumBlocks.PLANTER),
        18 * 6,
        18 * 1,
        RTPlantingRecipe.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTPlantingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)

        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTPlantingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
