package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.util.HTBounds
import hiiragi283.ragium.api.util.HTBoundsProvider
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTWidgetExtension : HTBoundsProvider {
    val font: Font get() = Minecraft.getInstance().font

    fun renderTooltip(
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
        width: Int = 18,
        height: Int = 18,
        action: () -> Unit,
    ) {
        if (HTBounds(x, y, width, height).contains(mouseX, mouseY)) {
            action()
        }
    }
}
