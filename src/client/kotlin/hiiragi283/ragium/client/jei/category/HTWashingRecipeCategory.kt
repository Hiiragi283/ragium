package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTWashingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory.Registered<HTWashingRecipe>(guiHelper, RagiumRecipeViewerTypes.WASHING, RagiumRecipeSerializers.WASHING) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTWashingRecipe, focuses: IFocusGroup) {
        // inputs
        val water: HTFluidIngredient = HTWashingRecipe.WATER_INGREDIENT
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidIngredient(water, false)
            .setFluidRenderer(water.amount.toLong(), false, 16, 18 * 3 - 2)
            .setTankBackground(HTBackgroundType.EXTRA_INPUT)

        builder
            .addInputSlot(getPosition(2), getPosition(1))
            .addItemIngredient(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        repeat(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(3 + index % 2), getPosition(0 + index / 2))
                .addItemResult(recipe.results.getOrNull(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTWashingRecipe, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(1))
        builder.addRecipePlus(getPosition(1), getPosition(1))
    }
}
