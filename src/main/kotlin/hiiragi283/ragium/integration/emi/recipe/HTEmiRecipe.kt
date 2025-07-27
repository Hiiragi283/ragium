package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.FillingArrowWidget
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder

interface HTEmiRecipe : EmiRecipe {
    fun getPosition(index: Int): Int = index * 18

    fun getPosition(index: Double): Int = (index * 18).toInt()

    fun WidgetHolder.addArrow(x: Int, y: Int): FillingArrowWidget = addFillingArrow(x, y, 2000)

    fun WidgetHolder.addInput(ingredient: EmiIngredient, x: Int, y: Int): SlotWidget = addSlot(ingredient, x, y)

    fun WidgetHolder.addOutput(
        stack: EmiStack?,
        x: Int,
        y: Int,
        large: Boolean = false,
    ): SlotWidget = when {
        large -> addSlot(stack ?: EmiStack.EMPTY, x - 4, y - 4).large(true)
        else -> addSlot(stack ?: EmiStack.EMPTY, x, y)
    }.recipeContext(this@HTEmiRecipe)
}
