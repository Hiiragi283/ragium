package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.capability.HTSlotHandler
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
}
