package hiiragi283.ragium.client.integration.jei.category

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.integration.jei.category.HTHolderRecipeCategory
import hiiragi283.ragium.api.recipe.RTRefiningRecipe
import hiiragi283.ragium.client.integration.jei.RagiumJeiRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

class RTRefiningRecipeCategory(guiHelper: IGuiHelper) : HTHolderRecipeCategory<RTRefiningRecipe>(guiHelper, RagiumJeiRecipeTypes.REFINING, 18 * 8, 18 * 1, RTRefiningRecipe.CODEC) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RTRefiningRecipe, focuses: IFocusGroup) {
        // inputs
        recipe.fluidIngredient.let {
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.INPUT, it.amount)
        }
        val itemInput: IRecipeSlotBuilder = builder.addInputSlot(getPosition(2), getPosition(0)).setSlotBackground(HTBackgroundType.INPUT)
        recipe.itemIngredient.onSome(itemInput::add)
        // outputs
        recipe.fluidResult.let {
            builder
                .addOutputSlot(getPosition(5), getPosition(0))
                .add(it)
                .setSlotBackground(HTBackgroundType.OUTPUT, it.amount)
        }
        val itemOutput: IRecipeSlotBuilder = builder.addInputSlot(getPosition(7), getPosition(0)).setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.itemResult.onSome(itemOutput::add)
    }

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RTRefiningRecipe, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
        builder.addRecipePlus(getPosition(6))
    }
}
