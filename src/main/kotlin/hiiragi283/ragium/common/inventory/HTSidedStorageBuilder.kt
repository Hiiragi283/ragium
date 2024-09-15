package hiiragi283.ragium.common.inventory

import net.minecraft.util.math.Direction

class HTSidedStorageBuilder(size: Int) {
    private val ioTypes: Array<HTStorageIO> = Array(size) { HTStorageIO.GENERIC }
    private val sides: Map<Direction, List<Int>>
        get() = sides1
    private val sides1: MutableMap<Direction, MutableList<Int>> = mutableMapOf()

    operator fun set(index: Int, type: HTStorageIO, sideType: HTStorageSides): HTSidedStorageBuilder = apply {
        ioTypes[index] = type
        sideType.directions.forEach { direction: Direction ->
            sides1.computeIfAbsent(direction) { mutableListOf() }.add(index)
        }
    }

    private fun getSideSlotArray(side: Direction): IntArray = sides[side]?.toIntArray() ?: intArrayOf()

    fun <T : Any> build(builder: (Array<HTStorageIO>, (Direction) -> IntArray) -> T): T = builder(ioTypes, ::getSideSlotArray)

    fun buildInventory(): HTSidedInventory = build(::HTSidedInventory)
}
