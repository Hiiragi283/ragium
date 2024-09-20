package hiiragi283.ragium.common.inventory

import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class HTSidedInventory(private val ioTypes: Array<HTStorageIO>, private val slotsMapper: (Direction) -> IntArray) :
    HTSimpleInventory(ioTypes.size),
    SidedInventory {
    //    SidedInventory    //

    override fun getAvailableSlots(side: Direction): IntArray = slotsMapper(side)

    override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?): Boolean =
        dir?.let { ioTypes[slot].canInsert && slot in slotsMapper(it) } ?: false

    override fun canExtract(slot: Int, stack: ItemStack, dir: Direction): Boolean = ioTypes[slot].canExtract && slot in slotsMapper(dir)
}
