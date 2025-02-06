package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.capability.HTSlotHandler
import hiiragi283.ragium.api.extension.forEachSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler

open class HTMachineItemHandler(size: Int, val callback: () -> Unit) : ItemStackHandler(size) {
    override fun onContentsChanged(slot: Int) {
        callback()
    }

    fun createSlot(slot: Int): HTSlotHandler<ItemStack> = object : HTSlotHandler<ItemStack> {
        override var stack: ItemStack
            get() = this@HTMachineItemHandler.getStackInSlot(slot)
            set(value) = this@HTMachineItemHandler.setStackInSlot(slot, value)
    }

    fun canConsumeAll(): Boolean {
        forEachSlot { slot: Int ->
            if (!consumeItem(slot, 1, true)) return false
        }
        return true
    }

    fun consumeItem(slot: Int, count: Int, simulate: Boolean): Boolean {
        val stackIn: ItemStack = getStackInSlot(slot)
        if (stackIn.isEmpty) return false
        if (stackIn.hasCraftingRemainingItem()) {
            val remainStack: ItemStack = stackIn.copy().craftingRemainingItem
            if (stackIn.count == 1 && remainStack.`is`(stackIn.item)) {
                if (!simulate) {
                    setStackInSlot(slot, stackIn.craftingRemainingItem)
                }
                return true
            }
        } else {
            if (!simulate) {
                stackIn.shrink(count)
            }
            return true
        }
        return false
    }
}
