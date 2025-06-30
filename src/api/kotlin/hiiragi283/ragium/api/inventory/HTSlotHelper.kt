package hiiragi283.ragium.api.inventory

object HTSlotHelper {
    @JvmStatic
    fun getSlotPosX(index: Int): Int = 8 + index * 18

    @JvmStatic
    fun getSlotPosY(index: Int): Int = 18 + index * 18

    @JvmStatic
    fun getSlotPosX(index: Double): Int = 8 + (index * 18).toInt()

    @JvmStatic
    fun getSlotPosY(index: Double): Int = 18 + (index * 18).toInt()

    @JvmStatic
    fun isIn(value: Int, start: Int, range: Int): Boolean = value in (start..start + range)
}
