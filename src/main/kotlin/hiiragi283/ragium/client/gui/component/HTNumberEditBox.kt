package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.function.clamp
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
            this.value = value.clamp(range).toString()
        }

    var range: RANGE = getDefaultRange()

    protected abstract fun getDefaultRange(): RANGE

    final override fun getBounds(): HTBounds = HTBounds(x, y, width, height)

    class IntRanged(
        font: Font,
        x: Int,
        y: Int,
        width: Int,
        height: Int = font.lineHeight + 3,
    ) : HTNumberEditBox<Int, IntRange>({ it.toIntOrNull() ?: 0 }, font, x, y, width, height) {
        override fun getDefaultRange(): IntRange = Int.MIN_VALUE..Int.MAX_VALUE
    }

    class LongRanged(
        font: Font,
        x: Int,
        y: Int,
        width: Int,
        height: Int = font.lineHeight + 3,
    ) : HTNumberEditBox<Long, LongRange>({ it.toLongOrNull() ?: 0 }, font, x, y, width, height) {
        override fun getDefaultRange(): LongRange = Long.MIN_VALUE..Long.MAX_VALUE
    }

    class DoubleRanged(
        font: Font,
        x: Int,
        y: Int,
        width: Int,
        height: Int = font.lineHeight + 3,
    ) : HTNumberEditBox<Double, ClosedFloatingPointRange<Double>>({ it.toDoubleOrNull() ?: 0.0 }, font, x, y, width, height) {
        override fun getDefaultRange(): ClosedFloatingPointRange<Double> = Double.MIN_VALUE..Double.MAX_VALUE
    }
}
