package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTWidget
import hiiragi283.ragium.api.math.HTBounds
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import java.util.function.Function

/**
 * @see me.desht.pneumaticcraft.client.gui.widget.WidgetTextFieldNumber
 */
@OnlyIn(Dist.CLIENT)
sealed class HTNumberEditBox<N, RANGE : ClosedRange<N>>(
    private val getter: Function<String, N>,
    font: Font,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : EditBox(
        font,
        x,
        y,
        width,
        height,
        Component.empty(),
    ),
    HTWidget where N : Number, N : Comparable<N> {
    var number: N
        get() = getter.apply(this.value)
        set(value) {
            this.value = value.toString()
        }

    var range: RANGE? = null

    final override fun getBounds(): HTBounds = HTBounds(x, y, width, height)

    class IntRanged(
        getter: Function<String, Int>,
        font: Font,
        x: Int,
        y: Int,
        width: Int,
        height: Int = font.lineHeight + 3,
    ) : HTNumberEditBox<Int, IntRange>(getter, font, x, y, width, height)

    class LongRanged(
        getter: Function<String, Long>,
        font: Font,
        x: Int,
        y: Int,
        width: Int,
        height: Int = font.lineHeight + 3,
    ) : HTNumberEditBox<Long, LongRange>(getter, font, x, y, width, height)

    class DoubleRanged(
        getter: Function<String, Double>,
        font: Font,
        x: Int,
        y: Int,
        width: Int,
        height: Int = font.lineHeight + 3,
    ) : HTNumberEditBox<Double, ClosedFloatingPointRange<Double>>(getter, font, x, y, width, height)
}
