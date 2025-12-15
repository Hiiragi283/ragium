package hiiragi283.ragium.api.math

/**
 * [HTBounds]を保持するインターフェース
 */
fun interface HTBoundsProvider {
    fun getBounds(): HTBounds

    fun isHovered(x: Int, y: Int): Boolean = getBounds().contains(x, y)
}
