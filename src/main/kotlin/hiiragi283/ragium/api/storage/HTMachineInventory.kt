package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.extension.isMaxCount
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

open class HTMachineInventory(size: Int, private val slotMap: Map<Int, HTStorageIO>) : SidedInventory {
    companion object {
        @JvmStatic
        fun ofSmall(): HTMachineInventory = Builder(2).input(0).output(1).build()

        @JvmStatic
        fun ofSimple(): HTMachineInventory = Builder(5).input(0, 1).output(3, 4).build()

        @JvmStatic
        fun ofLarge(): HTMachineInventory = Builder(7).input(0, 1, 2).output(4, 5, 6).build()
    }

    protected val inventory = SimpleInventory(size)
    protected val inputSlots: Set<Int> = slotMap.filter { (_: Int, storageIO: HTStorageIO) -> storageIO.canInsert }.keys

    fun getStorageIO(slot: Int): HTStorageIO = slotMap.getOrDefault(slot, HTStorageIO.INTERNAL)

    //    SidedInventory    //

    override fun getAvailableSlots(side: Direction): IntArray = IntArray(inventory.size()) { it }

    override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?): Boolean = getStorageIO(slot).canInsert

    override fun canExtract(slot: Int, stack: ItemStack, dir: Direction): Boolean = getStorageIO(slot).canExtract

    override fun size(): Int = inventory.size()

    override fun isEmpty(): Boolean = inventory.isEmpty()

    override fun getStack(slot: Int): ItemStack = inventory.getStack(slot)

    override fun removeStack(slot: Int, amount: Int): ItemStack = inventory.removeStack(slot, amount)

    override fun removeStack(slot: Int): ItemStack = inventory.removeStack(slot)

    override fun setStack(slot: Int, stack: ItemStack) {
        inventory.setStack(slot, stack)
    }

    override fun markDirty() {
        inventory.markDirty()
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean = inventory.canPlayerUse(player)

    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        if (getStorageIO(slot).canInsert) {
            inputSlots.map(::getStack).forEach { stackIn: ItemStack ->
                if (ItemStack.areItemsEqual(stack, stackIn) && stackIn.isMaxCount) {
                    return false
                }
            }
        }
        return super.isValid(slot, stack)
    }

    override fun clear() {
        inventory.clear()
    }

    //     Builder    //

    class Builder(maxSize: Int) {
        private val slotArray: Array<HTStorageIO> = Array(maxSize) { HTStorageIO.INTERNAL }

        private fun setIO(slot: Int, storageIO: HTStorageIO) {
            check(slotArray[slot] == HTStorageIO.INTERNAL) { "Slot: $slot is already modified!" }
            slotArray[slot] = storageIO
        }

        fun input(slots: IntRange): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.INPUT) }
        }

        fun input(vararg slots: Int): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.INPUT) }
        }

        fun output(slots: IntRange): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.OUTPUT) }
        }

        fun output(vararg slots: Int): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.OUTPUT) }
        }

        fun generic(slots: IntRange): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.GENERIC) }
        }

        fun generic(vararg slots: Int): Builder = apply {
            slots.forEach { setIO(it, HTStorageIO.GENERIC) }
        }

        fun build(): HTMachineInventory = HTMachineInventory(
            slotArray.size,
            slotArray.mapIndexed { slot: Int, storageIO: HTStorageIO -> slot to storageIO }.toMap(),
        )
    }
}
