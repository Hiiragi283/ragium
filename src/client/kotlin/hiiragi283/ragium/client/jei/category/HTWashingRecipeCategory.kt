package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import kotlin.jvm.optionals.getOrNull

class HTWashingRecipeCategory(guiHelper: IGuiHelper) :
    HTProcessingRecipeCategory<HTWashingRecipe>(guiHelper, RagiumJeiRecipeTypes.WASHING) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTWashingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(1))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTWashingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(true, recipe.fluidIngredient)
            .setTankBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addInputSlot(getPosition(1.5), getPosition(0.5))
            .addItemIngredient(recipe.itemIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(5), getPosition(0.5))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(5), getPosition(2))
            .addItemResult(recipe.extraResult.getOrNull())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }
}
