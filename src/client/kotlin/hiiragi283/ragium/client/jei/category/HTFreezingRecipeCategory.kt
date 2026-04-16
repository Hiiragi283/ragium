package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTFreezingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory.Registered<HTFreezingRecipe>(guiHelper, RagiumRecipeViewerTypes.FREEZING, RagiumRecipeSerializers.FREEZING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTFreezingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addItemIngredient(recipe.itemIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTFreezingRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(0))
    }
}
