package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.recipe.HTSingleItemRecipe
import hiiragi283.ragium.integration.jei.addResult
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.ItemLike

class HTSingleItemRecipeCategory<T : HTSingleItemRecipe>(
    guiHelper: IGuiHelper,
    private val recipeType: RecipeType<RecipeHolder<T>>,
    icon: ItemLike,
) : HTRecipeHolderCategory<T>(guiHelper, icon, 1.5) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: T) {
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

    override fun getRecipeType(): RecipeType<RecipeHolder<T>> = recipeType

    override fun getWidth(): Int = 18 * 4 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
