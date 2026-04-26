package hiiragi283.ragium.client.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.addFluidStacks
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.integration.jei.setTankRenderer
import hiiragi283.core.api.recipe.viewer.display.HTProcessingRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.config.RagiumConfig
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTWashingRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Processing(guiHelper, RagiumRecipeViewerTypes.WASHING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addFluidStacks(contents.inputFluid(0))
            .setTankRenderer(RagiumConfig.COMMON.machine.tankCapacity)
            .setTankBackground(HTBackgroundType.EXTRA_INPUT)

        builder
            .addInputSlot(getPosition(2), getPosition(1))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        // outputs
        repeat(4) { index: Int ->
            builder
                .addOutputSlot(getPosition(5 + index % 2), getPosition(0.5 + index / 2))
                .addChancedItem(contents.outputItem(index))
                .setSlotBackground(HTBackgroundType.OUTPUT)
        }
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProcessingRecipeDisplay, focuses: IFocusGroup) {
        builder.addAnimatedRecipeArrow(recipe.time).setPosition(getPosition(3.25), getPosition(1))
        builder.addRecipePlus(getPosition(1), getPosition(1))
    }
}
