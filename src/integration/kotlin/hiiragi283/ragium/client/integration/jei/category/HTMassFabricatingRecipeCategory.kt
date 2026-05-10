package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.core.api.text.toText
import hiiragi283.ragium.common.recipe.HTMassFabricatingRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.placement.HorizontalAlignment
import mezz.jei.api.gui.placement.VerticalAlignment
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class HTMassFabricatingRecipeCategory(guiHelper: IGuiHelper) : HTHolderRecipeCategory<HTMassFabricatingRecipe>(guiHelper, RagiumRecipeViewerTypes.MASS_FABRICATING, HTMassFabricatingRecipe.CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: HTMassFabricatingRecipe, focuses: IFocusGroup) {
        // input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .addItemStack(recipe.stack)
            .setSlotBackground(HTBackgroundType.INPUT)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTMassFabricatingRecipe, focuses: IFocusGroup) {
        builder.addRecipeArrow().setPosition(getPosition(1.25), getPosition(0))

        val textX: Int = getPosition(3)
        builder
            .addText("Matter Point: ${recipe.point}".toText(), width, 10)
            .setPosition(textX, 0, width - textX, height, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER)
            .setTextAlignment(HorizontalAlignment.RIGHT)
            .setTextAlignment(VerticalAlignment.CENTER)
            .setColor(0x808080)
    }
}
