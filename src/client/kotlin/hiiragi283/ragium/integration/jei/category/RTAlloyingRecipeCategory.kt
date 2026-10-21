package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTAlloyingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<HTSizedShapelessRecipe.Basic>(
        RagiumJeiRecipeTypes.ALLOYING,
        RagiumRecipeTypes.ALLOYING,
        guiHelper.createDrawableItemLike(RagiumBlocks.ALLOY_SMELTER),
        18 * 6,
        18 * 1,
        HTSizedShapelessRecipe.Basic.SIMPLE_CODEC
    ) {
    override fun setupRecipe(
        builder: IRecipeLayoutBuilder,
        recipe: HTSizedShapelessRecipe.Basic,
        focuses: IFocusGroup
    ) {
        // inputs
        val inputSlots: List<IRecipeSlotBuilder> = List(3) { index: Int ->
            builder
                .addInputSlot(getPosition(index), getPosition(0))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        inputSlots.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            recipe.ingredients.getOrNull(index)?.let(builder::add)
        }
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .add(recipe.result)
            .setSlotBackground(HTBackgroundType.OUTPUT)
    }

    override fun setupRecipeExtras(
        builder: IRecipeExtrasBuilder,
        recipe: HTSizedShapelessRecipe.Basic,
        focuses: IFocusGroup
    ) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }
}
