package hiiragi283.ragium.api.util

import java.awt.Color
import kotlin.math.max
import kotlin.math.min

data class HSVColor(val hue: Int, val saturation: Int, val brightness: Int) {
    companion object {
        @JvmField
        val BLACK = HSVColor(0, 0, 0)

        @JvmField
        val RED = HSVColor(0, 100, 255)

        @JvmField
        val GREEN = HSVColor(120, 100, 255)

        @JvmField
        val BLUE = HSVColor(240, 100, 255)

        @JvmField
        val WHITE = HSVColor(0, 0, 255)

        @JvmStatic
        fun fromRGB(color: Color): HSVColor {
            val rgb: List<Int> = listOf(color.red, color.green, color.blue)
            val max: Int = rgb.max()
            val min: Int = rgb.min()
            if (max == min) throw ArithmeticException("Unsupported color: $color")
            val diff: Int = max - min
            val hue: Float = 60 * when (max) {
                color.red -> (color.green - color.blue) / diff.toFloat()
                color.green -> ((color.blue - color.red) / diff.toFloat()) + 120
                color.blue -> ((color.red - color.green) / diff.toFloat()) + 240
                else -> throw IllegalStateException()
            }
            val saturation: Float = diff / max.toFloat()
            return HSVColor(hue.toInt(), saturation.toInt(), (max / 2.55f).toInt())
        }
    }

    init {
        check(hue in (0..360))
        check(saturation in (0..255))
        check(brightness in (0..255))
    }

    fun addHue(value: Int): HSVColor = copy(hue = min(360, max(0, hue + value)))

    fun addSaturation(value: Int): HSVColor = copy(saturation = min(255, max(0, saturation + value)))

    fun addBrightness(value: Int): HSVColor = copy(brightness = min(255, max(0, brightness + value)))

    fun toRGB(): Color {
        val max: Float = brightness.toFloat()
        val min: Float = max - ((saturation / 255f) * max)
        val diff: Float = max - min
        return when (hue) {
            in (0 until 60) -> createRGB(max, (hue / 60f) * diff + min, min)
            in (60 until 120) -> createRGB(((120 - hue) / 60f) * diff + min, max, min)
            in (120 until 180) -> createRGB(min, max, ((hue - 120) / 60f) * diff + min)
            in (180 until 240) -> createRGB(min, ((240 - hue) / 60f) * diff + min, max)
            in (240 until 300) -> createRGB(((hue - 240) / 60f) * diff + min, min, max)
            in (300..360) -> createRGB(max, min, ((360 - hue) / 60f) * diff + min)
            else -> throw IllegalStateException()
        }
    }

    private fun createRGB(red: Float, green: Float, blue: Float): Color = Color(red.toInt(), green.toInt(), blue.toInt())
}
