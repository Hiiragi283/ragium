package hiiragi283.ragium.api.inventory

import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class HTSidedInventory(
    size: Int,
    slotFilter: (Int, ItemStack) -> Boolean,
    private val ioMapper: (Int) -> HTStorageIO,
    private val slotsMapper: (Direction) -> IntArray,
) : HTSimpleInventory(size, slotFilter),
    SidedInventory {
    constructor(builder: HTSidedStorageBuilder) : this(
        builder.size,
        builder.slotFilter,
        builder.ioMapper,
        builder.sideMapper,
    )

    //    SidedInventory    //

    override fun getAvailableSlots(side: Direction): IntArray = slotsMapper(side)

    override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?): Boolean =
        dir?.let { ioMapper(slot).canInsert && slot in slotsMapper(it) } ?: false

    override fun canExtract(slot: Int, stack: ItemStack, dir: Direction): Boolean = ioMapper(slot).canExtract && slot in slotsMapper(dir)
}
