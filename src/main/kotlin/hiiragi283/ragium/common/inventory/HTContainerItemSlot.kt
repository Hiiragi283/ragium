package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.*
import kotlin.math.min

class HTContainerItemSlot(
    private val slot: HTItemSlot,
    private val storageIO: HTStorageIO,
    x: Int,
    y: Int,
) : Slot(emptyInventory, 0, x, y) {
    companion object {
        @JvmStatic
        private val emptyInventory = SimpleContainer(0)
    }

    override fun mayPlace(stack: ItemStack): Boolean {
        if (stack.isEmpty) return false
        return storageIO.canInsert && slot.canInsert(stack)
    }

    override fun getItem(): ItemStack = slot.getStack()

    override fun hasItem(): Boolean = !slot.isEmpty()

    override fun set(stack: ItemStack) {
        slot.setStack(stack)
        setChanged()
    }

    override fun setChanged() {
        super.setChanged()
        slot.onContentsChanged()
    }

    override fun getMaxStackSize(): Int = slot.getMaxSize(ItemStack.EMPTY)

    override fun getMaxStackSize(stack: ItemStack): Int = slot.getMaxSize(stack)

    override fun mayPickup(player: Player): Boolean = storageIO.canExtract && !slot.extractItem(1, true).isEmpty

    override fun remove(amount: Int): ItemStack {
        if (!storageIO.canExtract) return ItemStack.EMPTY
        return slot.extractItem(amount, false)
    }

    override fun tryRemove(count: Int, decrement: Int, player: Player): Optional<ItemStack> {
        if (!mayPickup(player)) return Optional.empty()
        val count1: Int = min(count, decrement)
        val removed: ItemStack = remove(count1)
        if (removed.isEmpty) {
            return Optional.empty()
        } else if (item.isEmpty) {
            setByPlayer(ItemStack.EMPTY, removed)
        }
        return Optional.of(removed)
    }
}
