package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.base.HTCombiningRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTCombiningRecipeCategory<RECIPE : HTCombiningRecipe>(
    private val maxInputs: Int,
    guiHelper: IGuiHelper,
    recipeType: HTHolderRecipeViewerType<*, RECIPE>,
) : HTProcessingRecipeCategory<RECIPE>(guiHelper, recipeType) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(maxInputs + 0.25), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // inputs
        for (i: Int in (0 until maxInputs)) {
            builder
                .addInputSlot(getPosition(i), getPosition(0))
                .addItemIngredient(recipe.ingredients.getOrNull(i))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        // output
        builder
            .addOutputSlot(getPosition(maxInputs + 2), getPosition(0))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }
}
