package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTChancedRecipeCategory
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.base.HTItemToChancedRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTItemToChancedRecipeCategory<RECIPE : HTItemToChancedRecipe>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderRecipeViewerType<*, RECIPE>,
) : HTChancedRecipeCategory<RECIPE>(guiHelper, recipeType) {
    companion object {
        @JvmStatic
        fun crushing(guiHelper: IGuiHelper): HTItemToChancedRecipeCategory<HTCrushingRecipe> =
            HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CRUSHING)

        @JvmStatic
        fun cutting(guiHelper: IGuiHelper): HTItemToChancedRecipeCategory<HTCuttingRecipe> =
            HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING)
    }

    override fun setupRecipeInput(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder
            .addInputSlot(getPosition(1.5), getPosition(0.5))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
    }
}
