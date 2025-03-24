package hiiragi283.ragium.integration.jei.category

import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.category.IRecipeCategory

abstract class HTRecipeCategory<T : Any>(protected val guiHelper: IGuiHelper) : IRecipeCategory<T> {
    fun getPosition(index: Int): Int = 5 + index * 18

    fun getPosition(index: Double): Int = 5 + (index * 18).toInt()
}
