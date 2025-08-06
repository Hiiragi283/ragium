package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.inventory.HTSlotHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTAbstractWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Component,
) : AbstractWidget(
        x,
        y,
        width,
        height,
        message,
    ) {
    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {}

    //    Extensions    //

    val font: Font get() = Minecraft.getInstance().font

    protected fun renderTooltip(
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
        rangeX: Int = 18,
        rangeY: Int = 18,
        action: () -> Unit,
    ) {
        if (HTSlotHelper.isIn(mouseX, x, rangeX) && HTSlotHelper.isIn(mouseY, y, rangeY)) {
            action()
        }
    }
}
