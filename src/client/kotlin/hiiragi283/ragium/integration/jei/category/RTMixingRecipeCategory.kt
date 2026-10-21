package hiiragi283.ragium.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.RTMixingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTMixingRecipeCategory(guiHelper: IGuiHelper) :
    HTHolderRecipeCategory<RTMixingRecipe>(
        RagiumJeiRecipeTypes.MIXING,
        RagiumRecipeTypes.MIXING,
        guiHelper.createDrawableItemLike(RagiumBlocks.MIXER),
        18 * 7,
        18 * 2,
        RTMixingRecipe.CODEC
    ) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTMixingRecipe, focuses: IFocusGroup) {
        // inputs
        val fluidInputs: List<IRecipeSlotBuilder> = List(2) { index: Int ->
            builder
                .addInputSlot(getPosition(0), getPosition(index))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        val itemInputs: List<IRecipeSlotBuilder> = List(4) { index: Int ->
            builder
                .addInputSlot(getPosition(2 + index % 2), getPosition(index / 2))
                .setSlotBackground(HTBackgroundType.INPUT)
        }
        val (items: List<HTItemIngredient>?, fluids: List<HTFluidIngredient>?) = recipe.ingredients.toPair()
        fluidInputs.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            fluids?.getOrNull(index)?.let(builder::add)
        }
        itemInputs.forEachIndexed { index: Int, builder: IRecipeSlotBuilder ->
            items?.getOrNull(index)?.let(builder::add)
        }
        // outputs
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(6), getPosition(1))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.content.mapLeft(itemOutput::add).mapRight(fluidOutput::add)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTMixingRecipe, focuses: IFocusGroup) {
        val posY: Int = getPosition(0.5)
        builder.addRecipePlus(getPosition(1), posY)
        builder.addRecipeArrow(recipe).setPosition(getPosition(4.25), posY)
    }
}
