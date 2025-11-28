package hiiragi283.ragium.client.integration.emi.widget

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.TankWidget
import hiiragi283.ragium.client.integration.emi.RagiumEmiTextures
import net.minecraft.client.gui.GuiGraphics

class HTTankWidget(
    stack: EmiIngredient?,
    x: Int,
    y: Int,
    capacity: Long,
) : TankWidget(
        stack ?: EmiStack.EMPTY,
        x,
        y,
        18,
        18 * 3,
        capacity,
    ) {
    override fun render(
        draw: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        RagiumEmiTextures.TANK.render(draw, x, y, delta)
        super.render(draw, mouseX, mouseY, delta)
    }
}
