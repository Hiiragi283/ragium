package hiiragi283.ragium.api.math

/**
 * 二次元平面上の範囲を表現するクラス
 * @param x x座標
 * @param y y座標
 * @param width x軸方向の範囲
 * @param height y軸方向の範囲
 * @see dev.emi.emi.api.widget.Bounds
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

    /**
     * x軸方向の範囲の最小値
     */
    val left: Int = x

    /**
     * x軸方向の範囲の最大値
     */
    val right: Int = x + width

    /**
     * y軸方向の範囲の最小値
     */
    val top: Int = y

    /**
     * y軸方向の範囲の最大値
     */
    val bottom: Int = y + height

    /**
     * [left]を起点とするx軸方向の範囲
     */
    val widthRange: IntRange = (left..<right)

    /**
     * [top]を起点とするy軸方向の範囲
     */
    val heightRange: IntRange = (top..<bottom)

    /**
     * 指定した[x], [y]がこの範囲に含まれているかどうか判定します。
     */
    fun contains(x: Int, y: Int): Boolean = x in widthRange && y in heightRange

    operator fun contains(provider: HTBoundsProvider): Boolean = contains(provider.getBounds())

    operator fun contains(bounds: HTBounds): Boolean {
        val bool1: Boolean = bounds.left in this.widthRange && bounds.right in this.widthRange
        val bool2: Boolean = bounds.top in this.heightRange && bounds.bottom in this.heightRange
        return bool1 && bool2
    }
}
