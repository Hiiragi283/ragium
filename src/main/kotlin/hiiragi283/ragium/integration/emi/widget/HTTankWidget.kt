package hiiragi283.ragium.integration.emi.widget

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.TankWidget
import hiiragi283.ragium.integration.emi.RagiumEmiTextures
import net.minecraft.client.gui.GuiGraphics
import java.util.function.LongSupplier

class HTTankWidget(
    stack: EmiIngredient?,
    x: Int,
    y: Int,
    capacity: LongSupplier,
) : TankWidget(
        stack ?: EmiStack.EMPTY,
        x,
        y,
        18,
        18 * 3,
        capacity.asLong,
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
