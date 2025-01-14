package hiiragi283.ragium.integration.jei.category

import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component

abstract class HTRecipeCategory<T : Any>(
    recipeType: RecipeType<T>,
    title: Component,
    icon: IDrawable,
    width: Int,
    height: Int,
) : AbstractRecipeCategory<T>(recipeType, title, icon, width, height) {
    protected fun getPosition(index: Int): Int = 5 + index * 18
}
