package hiiragi283.ragium.api.gui.component

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component

/**
 * @see [me.desht.pneumaticcraft.client.gui.widget.WidgetTextFieldNumber]
 */
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
    ) where N : Number, N : Comparable<N> {
    var number: N
        get() = value.let(getter)
        set(value) {
            this.value = value.toString()
        }

    var range: ClosedRange<N>? = null
}
