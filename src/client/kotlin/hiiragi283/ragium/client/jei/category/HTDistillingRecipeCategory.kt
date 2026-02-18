package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTDistillingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

@Suppress("FINAL_UPPER_BOUND")
class HTDistillingRecipeCategory(guiHelper: IGuiHelper) :
    HTProcessingRecipeCategory<HTDistillingRecipe>(guiHelper, RagiumJeiRecipeTypes.DISTILLING) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTDistillingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTDistillingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addFluidSlot(getPosition(0), getPosition(0), false, recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val (itemOut: List<HTItemResult>?, fluidOut: List<HTFluidResult>?) = recipe.results.toPair()
        builder
            .addItemSlot(getPosition(3), getPosition(0), itemOut?.firstOrNull())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)

        for (i: Int in 0 until HTDistillingRecipe.MAX_FLUID_OUTPUT) {
            builder
                .addFluidSlot(getPosition(4 + i), getPosition(0), false, fluidOut?.getOrNull(i))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }
}
