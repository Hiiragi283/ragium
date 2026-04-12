package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTLookupRecipeCategory
import hiiragi283.ragium.client.jei.RagiumJeiRecipeTypes
import hiiragi283.ragium.common.recipe.HTElectrolyzingRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTElectrolyzingRecipeCategory(guiHelper: IGuiHelper) :
    HTLookupRecipeCategory<HTElectrolyzingRecipe>(guiHelper, RagiumJeiRecipeTypes.ELECTROLYZING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTElectrolyzingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .addItemResult(recipe.extraResult.getLeft())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)

        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addFluidResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
        builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .addFluidResult(recipe.extraResult.getRight())
            .setSlotBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    override fun createRecipeExtrasImpl(builder: IRecipeExtrasBuilder, recipe: HTElectrolyzingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }
}
