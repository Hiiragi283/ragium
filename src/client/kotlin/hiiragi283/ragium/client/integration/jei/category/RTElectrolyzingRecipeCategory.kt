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
        18 * 6,
        18 * 1,
        RTElectrolyzingRecipe.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTElectrolyzingRecipe, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.ingredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        val outputSlots: Array<IRecipeSlotBuilder> = Array(3) { index: Int ->
            builder.addOutputSlot(getPosition(3 + index), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT)
        }
        for ((index: Int, result: HTFluidResult) in recipe.results.withIndex()) {
            outputSlots[index].add(result)
        }
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTElectrolyzingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
    }
}
