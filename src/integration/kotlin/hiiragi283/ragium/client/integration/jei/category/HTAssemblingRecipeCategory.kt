package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.addChancedItem
import hiiragi283.core.api.integration.jei.category.HTDisplayRecipeCategory
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import net.minecraft.world.item.ItemStack

class HTAssemblingRecipeCategory(guiHelper: IGuiHelper) : HTDisplayRecipeCategory.Progress(guiHelper, RagiumRecipeViewerTypes.ASSEMBLING) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, contents: HTRecipeContents, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemStacks(contents.inputItem(0))
            .setSlotBackground(HTBackgroundType.INPUT)
        val secondary: List<ItemStack> = contents.inputItem(1)
        if (secondary.isEmpty()) {
            builder
                .addSlot(RecipeIngredientRole.CATALYST, getPosition(2), getPosition(0))
                .addItemStacks(contents.catalyst(0))
                .setSlotBackground(HTBackgroundType.NONE)
        } else {
            builder
                .addInputSlot(getPosition(2), getPosition(0))
                .addItemStacks(secondary)
                .setSlotBackground(HTBackgroundType.EXTRA_INPUT)
        }
        // output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .addChancedItem(contents.outputItem(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTProgressRecipeDisplay, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(1))
    }
}
