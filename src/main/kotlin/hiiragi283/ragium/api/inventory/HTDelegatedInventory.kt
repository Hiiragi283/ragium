package hiiragi283.ragium.api.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

sealed interface HTDelegatedInventory<T : HTSimpleInventory> : Inventory {
    val parent: T

    override fun clear() {
        parent.clear()
    }

    override fun size(): Int = parent.size()

    override fun isEmpty(): Boolean = parent.isEmpty

    override fun getStack(slot: Int): ItemStack = parent.getStack(slot)

    override fun removeStack(slot: Int, amount: Int): ItemStack = parent.removeStack(slot, amount)

    override fun removeStack(slot: Int): ItemStack = parent.removeStack(slot)

    override fun setStack(slot: Int, stack: ItemStack) {
        parent.setStack(slot, stack)
    }

    override fun markDirty() {
        parent.markDirty()
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean = parent.canPlayerUse(player)

    //    Simple    //

    interface Simple : HTDelegatedInventory<HTSimpleInventory>

    //    Sided    //

    interface Sided :
        HTDelegatedInventory<HTSidedInventory>,
        SidedInventory {
        override fun getAvailableSlots(side: Direction): IntArray = parent.getAvailableSlots(side)

        override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?): Boolean = parent.canInsert(slot, stack, dir)

        override fun canExtract(slot: Int, stack: ItemStack, dir: Direction): Boolean = parent.canExtract(slot, stack, dir)
    }
}
