package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.TextureWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.inventory.HTSlotPos
import net.minecraft.resources.ResourceLocation

interface HTEmiRecipe : EmiRecipe {
    fun getPosition(index: Int): Int = index * 18

    fun getPosition(index: Double): Int = (index * 18).toInt()

    fun WidgetHolder.addArrow(x: Double, y: Double): TextureWidget = addFillingArrow(getPosition(x), getPosition(y), 2000)

    fun WidgetHolder.addInput(ingredient: EmiIngredient, x: Double, y: Double): SlotWidget =
        addSlot(ingredient, getPosition(x), getPosition(y))

    fun WidgetHolder.addOutput(stack: EmiStack, x: Double, y: Double): SlotWidget =
        addSlot(stack, getPosition(x), getPosition(y)).recipeContext(this@HTEmiRecipe)

    //    Base    //

    abstract class Base(private val id: ResourceLocation, private val textureId: ResourceLocation) : HTEmiRecipe {
        final override fun getId(): ResourceLocation = id

        final override fun getDisplayWidth(): Int = getPosition(7)

        final override fun getDisplayHeight(): Int = getPosition(3)

        override fun addWidgets(widgets: WidgetHolder) {
            widgets.addTexture(
                EmiTexture(
                    textureId,
                    HTSlotPos.getSlotPosX(1) - 1,
                    HTSlotPos.getSlotPosY(0) - 1,
                    getPosition(6),
                    getPosition(3),
                ),
                0,
                0,
            )
            widgets.addArrow(2.5, 1.0)
        }

        protected fun WidgetHolder.addOutput(index: Int, x: Double, y: Double): SlotWidget =
            addOutput(outputs.getOrNull(index) ?: EmiStack.EMPTY, x, y).drawBack(false)
    }
}
