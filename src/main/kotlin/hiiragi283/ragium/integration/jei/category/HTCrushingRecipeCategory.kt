package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.recipe.HTCrushingRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addResult
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HTCrushingRecipeCategory(guiHelper: IGuiHelper) : HTRecipeHolderCategory<HTCrushingRecipe>(guiHelper, Items.CRAFTING_TABLE, 1.5) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTCrushingRecipe) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.ingredient)
        // Item Output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addResult(recipe)
    }

    override fun getRecipeType(): RecipeType<RecipeHolder<HTCrushingRecipe>> = RagiumJEIRecipeTypes.CRUSHING

    override fun getWidth(): Int = 18 * 4 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
