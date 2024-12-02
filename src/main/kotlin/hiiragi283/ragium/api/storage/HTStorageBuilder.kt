package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedSlottedStorage
import net.minecraft.inventory.SidedInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import kotlin.math.min

class HTStorageBuilder(val size: Int) {
    val sizeRange: IntRange = (0 until size)

    val ioMapper: (Int) -> HTStorageIO
        get() = ioTypes::get
    private val ioTypes: Array<HTStorageIO> = Array(size) { HTStorageIO.GENERIC }

    val sideMapper: (Direction) -> IntArray = { sides[it]?.toIntArray() ?: intArrayOf() }
    private val sides: MutableMap<Direction, MutableList<Int>> = mutableMapOf()

    operator fun set(index: Int, type: HTStorageIO, sideType: HTStorageSide): HTStorageBuilder = apply {
        ioTypes[index] = type
        sideType.directions.forEach { direction: Direction ->
            sides.computeIfAbsent(direction) { mutableListOf() }.add(index)
        }
    }

    fun setAll(type: HTStorageIO, sideType: HTStorageSide, indices: IntRange): HTStorageBuilder = apply {
        indices.forEach { index: Int ->
            set(index, type, sideType)
        }
    }

    var stackFilter: (Int, ItemStack) -> Boolean = { _: Int, _: ItemStack -> true }
        private set

    fun stackFilter(filter: (Int, ItemStack) -> Boolean): HTStorageBuilder = apply {
        this.stackFilter = filter
    }

    var fluidFilter: (Int, FluidVariant) -> Boolean = { _: Int, _: FluidVariant -> true }
        private set

    fun fluidFilter(filter: (Int, FluidVariant) -> Boolean): HTStorageBuilder = apply {
        this.fluidFilter = filter
    }

    var countFilter: (ItemStack) -> Int = { min(99, it.maxCount) }
        private set

    fun countFilter(filter: (ItemStack) -> Int): HTStorageBuilder = apply {
        this.countFilter = filter
    }

    fun <T : Any> build(builder: (HTStorageBuilder) -> T): T = builder(this)

    fun buildInventory(): SidedInventory = build(::SidedInventoryImpl)

    fun buildItemStorage(capacity: Long): CombinedSlottedStorage<ItemVariant, SingleItemStorage> =
        CombinedSlottedStorage(sizeRange.map { ioMapper(it).createItemStorage(capacity) })

    fun buildFluidStorage(capacity: Long): CombinedSlottedStorage<FluidVariant, SingleFluidStorage> =
        CombinedSlottedStorage(sizeRange.map { ioMapper(it).createFluidStorage(capacity) })

    fun buildMachineFluidStorage(): HTMachineFluidStorage = build(::HTMachineFluidStorage)

    //    SidedInventoryImpl    //

    private class SidedInventoryImpl(
        size: Int,
        private val ioMapper: (Int) -> HTStorageIO,
        private val slotsMapper: (Direction) -> IntArray,
        private val slotFilter: (Int, ItemStack) -> Boolean,
        private val countFilter: (ItemStack) -> Int,
    ) : SimpleInventory(size),
        SidedInventory {
        constructor(builder: HTStorageBuilder) : this(
            builder.size,
            builder.ioMapper,
            builder.sideMapper,
            builder.stackFilter,
            builder.countFilter,
        )

        //    Inventory    //

        override fun setStack(slot: Int, stack: ItemStack) {
            if (!isValid(slot, stack)) return
            super.setStack(slot, stack)
        }

        override fun isValid(slot: Int, stack: ItemStack): Boolean = slotFilter(slot, stack)

        override fun getMaxCount(stack: ItemStack): Int = countFilter(stack)

        //    SidedInventory    //

        override fun getAvailableSlots(side: Direction): IntArray = slotsMapper(side)

        override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?): Boolean =
            dir?.let { ioMapper(slot).canInsert && slot in slotsMapper(it) } == true

        override fun canExtract(slot: Int, stack: ItemStack, dir: Direction): Boolean =
            ioMapper(slot).canExtract && slot in slotsMapper(dir)
    }
}
