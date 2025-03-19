package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.recipe.HTCentrifugingRecipe
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

class HTCentrifugingRecipeCategory(guiHelper: IGuiHelper) :
    HTRecipeHolderCategory<HTCentrifugingRecipe>(guiHelper, RagiumBlocks.CRUSHER, 1.5, 1.0) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTCentrifugingRecipe) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(1))
            .setStandardSlotBackground()
            .addIngredients(recipe.ingredient)
        // Item Output
        builder
            .addOutputSlot(getPosition(3), getPosition(1))
            .setStandardSlotBackground()
            .addItemStack(recipe.getResultItem(0))

        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.getResultItem(1))

        builder
            .addOutputSlot(getPosition(5), getPosition(1))
            .setStandardSlotBackground()
            .addItemStack(recipe.getResultItem(2))

        builder
            .addOutputSlot(getPosition(4), getPosition(2))
            .setStandardSlotBackground()
            .addItemStack(recipe.getResultItem(3))
    }

    override fun getRecipeType(): RecipeType<RecipeHolder<HTCentrifugingRecipe>> = RagiumJEIRecipeTypes.CENTRIFUGING

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 3 + 8
}
