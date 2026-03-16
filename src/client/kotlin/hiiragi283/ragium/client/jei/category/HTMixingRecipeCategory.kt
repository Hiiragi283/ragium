package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.client.jei.category.base.HTProcessingRecipeCategory
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTMixingRecipeCategory(guiHelper: IGuiHelper) :
    HTProcessingRecipeCategory<HTMixingRecipe>(guiHelper, RagiumJeiRecipeTypes.MIXING) {
    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTMixingRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(4.25), getPosition(0))
        builder.addRecipePlus(getPosition(7))
    }

    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTMixingRecipe, focuses: IFocusGroup) {
        // inputs
        val (item: HTItemIngredient?, fluids: List<HTFluidIngredient>?) = recipe.ingredient.toPair()
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemIngredient(item)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)

        for (i: Int in (0 until HTMixingRecipe.MAX_FLUID_INPUT)) {
            builder
                .addInputSlot(getPosition(i + 2), getPosition(0))
                .addFluidIngredient(fluids?.getOrNull(i))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        // outputs
        builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .addItemResult(recipe.result.getLeft())
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(8), getPosition(0))
            .addFluidResult(recipe.result.getRight())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }
}
