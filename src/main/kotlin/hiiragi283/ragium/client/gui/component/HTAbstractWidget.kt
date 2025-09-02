package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTWidgetExtension
import hiiragi283.ragium.api.math.HTBounds
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
    ),
    HTWidgetExtension {
    final override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {}

    //    Extensions    //

    final override fun getBounds(): HTBounds = HTBounds(x, y, width, height)
}
