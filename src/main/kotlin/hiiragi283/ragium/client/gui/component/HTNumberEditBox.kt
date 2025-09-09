package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.math.HTBoundsProvider
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see [me.desht.pneumaticcraft.client.gui.widget.WidgetTextFieldNumber]
 */
@OnlyIn(Dist.CLIENT)
class HTNumberEditBox<N>(
    private val getter: (String) -> N,
    font: Font,
    x: Int,
    y: Int,
    width: Int,
    height: Int = font.lineHeight + 3,
) : EditBox(
        font,
        x,
        y,
        width,
        height,
        Component.empty(),
    ),
    HTBoundsProvider where N : Number, N : Comparable<N> {
    var number: N
        get() = value.let(getter)
        set(value) {
            this.value = value.toString()
        }

    var range: ClosedRange<N>? = null

    override fun getBounds(): HTBounds = HTBounds(x, y, width, height)
}
