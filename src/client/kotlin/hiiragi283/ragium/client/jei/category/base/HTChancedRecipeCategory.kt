package hiiragi283.ragium.client.jei.category.base

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

abstract class HTChancedRecipeCategory<RECIPE : HTChancedRecipe<*>>(
    guiHelper: IGuiHelper,
    recipeType: HTRecipeViewerType<RecipeHolder<RECIPE>>,
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
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)

        builder
            .addOutputSlot(getPosition(5.5), getPosition(2))
            .addItemResult(recipe.extraResult.getOrNull())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    protected abstract fun setupRecipeInput(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup)
}
