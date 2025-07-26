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

    fun WidgetHolder.addArrow(x: Double, y: Double): FillingArrowWidget = addFillingArrow(getPosition(x), getPosition(y), 2000)

    fun WidgetHolder.addInput(ingredient: EmiIngredient, x: Double, y: Double): SlotWidget =
        addSlot(ingredient, getPosition(x), getPosition(y))

    fun WidgetHolder.addOutput(stack: EmiStack, x: Double, y: Double): SlotWidget =
        addSlot(stack, getPosition(x), getPosition(y)).recipeContext(this@HTEmiRecipe)
}
