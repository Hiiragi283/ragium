package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.inventory.HTSlotPos
import net.minecraft.resources.ResourceLocation

abstract class HTMachineEmiRecipe(private val id: ResourceLocation, private val textureId: ResourceLocation) : HTEmiRecipe {
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
