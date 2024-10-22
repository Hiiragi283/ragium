package hiiragi283.ragium.api.inventory

import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class HTStorageBuilder(val size: Int) {
    companion object {
        @JvmField
        val ACCEPT_ALL: (Int, ItemStack) -> Boolean = { _: Int, _: ItemStack -> true }
    }

    val ioMapper: (Int) -> HTStorageIO
        get() = ioTypes::get
    private val ioTypes: Array<HTStorageIO> = Array(size) { HTStorageIO.GENERIC }

    val sideMapper: (Direction) -> IntArray = { sides[it]?.toIntArray() ?: intArrayOf() }
    private val sides: MutableMap<Direction, MutableList<Int>> = mutableMapOf()

    var slotFilter: (Int, ItemStack) -> Boolean = ACCEPT_ALL
        private set

    operator fun set(index: Int, type: HTStorageIO, sideType: HTStorageSide): HTStorageBuilder = apply {
        ioTypes[index] = type
        sideType.directions.forEach { direction: Direction ->
            sides.computeIfAbsent(direction) { mutableListOf() }.add(index)
        }
    }

    fun setAll(type: HTStorageIO, sideType: HTStorageSide, indices: IntRange): HTStorageBuilder = apply {
        indices.forEach { set(it, type, sideType) }
    }

    fun setAll(type: HTStorageIO, sideType: HTStorageSide, vararg indices: Int): HTStorageBuilder = apply {
        indices.forEach { set(it, type, sideType) }
    }

    fun filter(filter: (Int, ItemStack) -> Boolean): HTStorageBuilder = apply {
        this.slotFilter = filter
    }

    fun <T : Any> build(builder: (HTStorageBuilder) -> T): T = builder(this)

    fun buildSimple(): HTSimpleInventory = build(::HTSimpleInventory)

    fun buildSided(): HTSidedInventory = build(::HTSidedInventory)
}
