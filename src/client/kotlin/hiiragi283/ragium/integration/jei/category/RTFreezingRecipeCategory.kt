package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTSingleAndCatalystRecipeCategory
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.helpers.IGuiHelper

class RTFreezingRecipeCategory(guiHelper: IGuiHelper) :
    HTSingleAndCatalystRecipeCategory<RTFreezingRecipe>(
        RagiumJeiRecipeTypes.FREEZING,
        RagiumRecipeTypes.FREEZING,
        guiHelper.createDrawableItemLike(RagiumBlocks.FREEZER),
        RTFreezingRecipe.CODEC
    ) {
    override fun setInput(builder: IRecipeSlotBuilder, recipe: RTFreezingRecipe) {
        builder.add(recipe.result)
    }

    override fun setOutput(builder: IRecipeSlotBuilder, recipe: RTFreezingRecipe) {
        builder.add(recipe.result)
    }
}
