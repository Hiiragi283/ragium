package hiiragi283.lib.gui

/**
 * GUI上での座標に関するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTSlotHelper {
    @JvmStatic
    fun getSlotPosX(index: Int): Int = 8 + index * 18

    @JvmStatic
    fun getSlotPosY(index: Int): Int = 18 + index * 18

    @JvmStatic
    fun getSlotPosX(index: Double): Int = 8 + (index * 18).toInt()

    @JvmStatic
    fun getSlotPosY(index: Double): Int = 18 + (index * 18).toInt()
}
