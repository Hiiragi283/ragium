package hiiragi283.ragium.common.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class HTModularToolInventory : Inventory {
    var parent: ItemStack? = null

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getStack(slot: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun removeStack(slot: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun setStack(slot: Int, stack: ItemStack?) {
        TODO("Not yet implemented")
    }

    override fun markDirty() {
        TODO("Not yet implemented")
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean = true
}
