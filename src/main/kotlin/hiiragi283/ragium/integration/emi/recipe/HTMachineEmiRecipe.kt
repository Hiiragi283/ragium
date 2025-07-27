package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.inventory.HTSlotHelper
import net.minecraft.resources.ResourceLocation

abstract class HTMachineEmiRecipe(private val id: ResourceLocation, private val textureId: ResourceLocation) : HTEmiRecipe {
    final override fun getId(): ResourceLocation = id

    final override fun getDisplayWidth(): Int = getPosition(7)

    final override fun getDisplayHeight(): Int = getPosition(3)

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(
            EmiTexture(
                textureId,
                HTSlotHelper.getSlotPosX(1) - 1,
                HTSlotHelper.getSlotPosY(0) - 1,
                getPosition(6),
                getPosition(3),
            ),
            0,
            0,
        )
        widgets.addArrow(getPosition(2.5), getPosition(1))
    }

    protected fun WidgetHolder.addOutput(
        index: Int,
        x: Int,
        y: Int,
        large: Boolean = false,
    ): SlotWidget = addOutput(outputs.getOrNull(index), x, y, large).drawBack(false)
}
