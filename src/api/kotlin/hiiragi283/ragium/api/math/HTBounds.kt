package hiiragi283.ragium.api.math

/**
 * @see [dev.emi.emi.api.widget.Bounds]
 */
data class HTBounds(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
) {
    companion object {
        @JvmField
        val EMPTY = HTBounds(0, 0, 0, 0)

        @JvmStatic
        fun createSlot(x: Int, y: Int): HTBounds = HTBounds(x, y, 18, 18)
    }

    val left: Int = x
    val right: Int = x + width
    val top: Int = y
    val bottom: Int = y + height

    val widthRange: IntRange = (left..<right)
    val heightRange: IntRange = (top..<bottom)

    fun contains(x: Int, y: Int): Boolean = x in widthRange && y in heightRange
}
