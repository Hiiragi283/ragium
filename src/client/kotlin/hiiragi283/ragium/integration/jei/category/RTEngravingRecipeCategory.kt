package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTSingleAndCatalystRecipeCategory
import hiiragi283.ragium.api.recipe.RTEngravingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.helpers.IGuiHelper

class RTEngravingRecipeCategory(guiHelper: IGuiHelper) :
    HTSingleAndCatalystRecipeCategory<RTEngravingRecipe>(
        RagiumJeiRecipeTypes.ENGRAVING,
        RagiumRecipeTypes.ENGRAVING,
        guiHelper.createDrawableItemLike(RagiumBlocks.ENGRAVER),
        RTEngravingRecipe.CODEC
    ) {
    override fun setInput(builder: IRecipeSlotBuilder, recipe: RTEngravingRecipe) {
        builder.add(recipe.ingredient)
    }

    override fun setOutput(builder: IRecipeSlotBuilder, recipe: RTEngravingRecipe) {
        builder.add(recipe.result)
    }
}
