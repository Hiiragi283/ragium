package hiiragi283.ragium.integration.emi.widget

import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.Widget
import hiiragi283.ragium.api.gui.component.HTBackgroundRenderable
import hiiragi283.ragium.api.gui.component.HTWidget
import hiiragi283.ragium.integration.emi.toEmi
import net.minecraft.client.gui.GuiGraphics

open class HTEmiWidget(private val delegate: HTWidget) : Widget() {
    private val bounds: Bounds = delegate.getBounds().toEmi()

    override fun getBounds(): Bounds = bounds

    override fun render(
        draw: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        if (delegate is HTBackgroundRenderable) {
            delegate.renderBackground(draw)
        }
        delegate.render(draw, mouseX, mouseY, delta)
    }
}
