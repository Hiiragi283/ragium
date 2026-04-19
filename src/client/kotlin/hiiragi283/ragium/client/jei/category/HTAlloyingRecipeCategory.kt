package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTAlloyingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory.Registered<HTAlloyingRecipe>(guiHelper, RagiumRecipeViewerTypes.ALLOYING, RagiumRecipeSerializers.ALLOYING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTAlloyingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemIngredient(recipe.primary)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .addItemIngredient(recipe.secondary)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        builder
            .addInputSlot(getPosition(3), getPosition(0))
            .addItemIngredient(recipe.tertiary)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        // output
        builder
            .addInputSlot(getPosition(6), getPosition(0))
            .addItemResult(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTAlloyingRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(4.25), getPosition(0))
    }
}
