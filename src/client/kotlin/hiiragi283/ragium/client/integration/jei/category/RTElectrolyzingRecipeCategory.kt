package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTElectrolyzingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTElectrolyzingRecipe>(
        guiHelper,
        RagiumJeiRecipeTypes.ELECTROLYZING,
        18 * 8,
        18 * 1,
        RTElectrolyzingRecipe.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTElectrolyzingRecipe, focuses: IFocusGroup) {
        // inputs
        recipe.fluidIngredient.let {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.INPUT, it.amount)
        }
        val itemInput: IRecipeSlotBuilder = builder.addInputSlot(
            getPosition(2),
            getPosition(0)
        ).setSlotBackground(HTBackgroundType.INPUT)
        recipe.itemIngredient.ifPresent(itemInput::add)
        // outputs
        val outputSlots: Array<IRecipeSlotBuilder> =
            Array(3) { index: Int ->
                builder.addOutputSlot(getPosition(5 + index), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT)
            }
        for (index: Int in recipe.results.indices) {
            val result: HTFluidResult = recipe.results[index]
            outputSlots[index]
                .add(result)
                .setSlotBackground(HTBackgroundType.OUTPUT, result.amount)
        }
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTElectrolyzingRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
