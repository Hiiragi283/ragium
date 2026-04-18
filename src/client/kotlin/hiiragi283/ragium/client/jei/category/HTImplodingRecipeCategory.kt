package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTImplodingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory.Registered<HTImplodingRecipe>(guiHelper, RagiumRecipeViewerTypes.IMPLODING, RagiumRecipeSerializers.IMPLODING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTImplodingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0.5))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0.5))
            .addItemIngredient(recipe.explosive)
            .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        // outputs
        repeat(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(5 + index % 2), getPosition(0 + index / 2))
                .addItemResult(recipe.results.getOrNull(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTImplodingRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1), getPosition(0.5))
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(0.5))
    }
}
