package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder

interface HTSimpleEmiRecipe : HTEmiRecipe {
    override fun getDisplayWidth(): Int = getPosition(3.5)

    override fun getDisplayHeight(): Int = getPosition(1)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addInput(getFirstInput(), 0.0, 0.0)
        widgets.addTexture(EmiTexture.EMPTY_ARROW, getPosition(1), getPosition(0))
        widgets.addOutput(getFirstOutput(), 2.5, 0.0)
    }

    fun getFirstInput(): EmiIngredient

    fun getFirstOutput(): EmiStack
}
