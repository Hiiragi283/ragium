package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.FillingArrowWidget
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.TankWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.integration.emi.RagiumEmiTextures

interface HTEmiRecipe : EmiRecipe {
    fun getPosition(index: Int): Int = index * 18

    fun getPosition(index: Double): Int = (index * 18).toInt()

    fun WidgetHolder.addArrow(x: Int, y: Int): FillingArrowWidget = addFillingArrow(x, y, 2000)

    fun WidgetHolder.addOutput(
        result: EmiIngredient?,
        x: Int,
        y: Int,
        large: Boolean = false,
    ): SlotWidget = when {
        large -> addSlot(result ?: EmiStack.EMPTY, x - 4, y - 4).large(true)
        else -> addSlot(result ?: EmiStack.EMPTY, x, y)
    }.recipeContext(this@HTEmiRecipe).drawBack(false)

    fun WidgetHolder.addTank(result: EmiIngredient?, x: Int, y: Int): TankWidget {
        addTexture(
            RagiumEmiTextures.TANK,
            x,
            y,
        )
        return addTank(
            result ?: EmiStack.EMPTY,
            x,
            y,
            18,
            18 * 3,
            8000,
        )
    }
}
