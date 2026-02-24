package hiiragi283.ragium.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.viewer.HTHolderRecipeViewerType
import hiiragi283.ragium.api.recipe.HTChancedRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

abstract class HTChancedRecipeCategory<RECIPE : HTChancedRecipe.Serializable<*>>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderRecipeViewerType<*, RECIPE>,
) : HTProcessingRecipeCategory<RECIPE>(guiHelper, recipeType) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.5), getPosition(1))
    }

    final override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // inputs
        setupRecipeInput(builder, recipe, focuses)
        // outputs
        builder
            .addOutputSlot(getPosition(5.5), getPosition(0.5))
            // .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)

        builder
            .addOutputSlot(getPosition(5.5), getPosition(2))
            // .addItemResult(recipe.extraResult.getOrNull())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    protected abstract fun setupRecipeInput(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)
}
