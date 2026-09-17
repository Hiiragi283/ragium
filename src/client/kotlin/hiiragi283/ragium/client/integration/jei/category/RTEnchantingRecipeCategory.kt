package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTDoubleItemToRecipeCategory
import hiiragi283.ragium.api.recipe.RTEnchantingRecipe
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.helpers.IGuiHelper

class RTEnchantingRecipeCategory(guiHelper: IGuiHelper) :
    HTDoubleItemToRecipeCategory<RTEnchantingRecipe>(
        guiHelper,
        RagiumJeiRecipeTypes.ENCHANTING,
        RTEnchantingRecipe.CODEC
    ) {
    override fun setPrimaryInput(builder: IRecipeSlotBuilder, recipe: RTEnchantingRecipe) {
        builder.add(RTEnchantingRecipe.BOOK_INGREDIENT)
    }

    override fun setSecondaryInput(builder: IRecipeSlotBuilder, recipe: RTEnchantingRecipe) {
        builder.add(recipe.ingredient)
    }

    override fun setOutput(builder: IRecipeSlotBuilder, recipe: RTEnchantingRecipe) {
        builder.add(recipe.result.create())
    }
}
