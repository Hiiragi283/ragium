package hiiragi283.ragium.api.inventory.slot

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.Optional
import java.util.function.Consumer
import kotlin.math.min

/**
 * [HTItemSlot]に基づいた[Slot]の実装
 * @see [mekanism.common.inventory.container.slot.InventoryContainerSlot]
 */
open class HTContainerItemSlot(
    val slot: HTItemSlot,
    x: Int,
    y: Int,
    private val uncheckedSetter: Consumer<ItemStack>,
) : Slot(emptyContainer, 0, x, y),
    HTSlot {
    companion object {
        @JvmStatic
        private val emptyContainer = SimpleContainer(0)
    }

    override fun insertItem(stack: ItemStack, simulate: Boolean): ItemStack {
        val remainder: ItemStack = slot.insertItem(stack, simulate, HTStorageAccess.MANUAL)
        if (!simulate && stack.count != remainder.count) {
            setChanged()
        }
        return remainder
    }

    override fun mayPlace(stack: ItemStack): Boolean {
        if (stack.isEmpty) return false
        if (slot.isEmpty) return insertItem(stack, true).count < stack.count
        if (slot.extractItem(1, true, HTStorageAccess.MANUAL).isEmpty) return false
        return slot.isItemValidForInsert(stack, HTStorageAccess.MANUAL)
    }

    override fun getItem(): ItemStack = slot.getStack()

    override fun hasItem(): Boolean = !slot.isEmpty

    override fun set(stack: ItemStack) {
        uncheckedSetter.accept(stack)
        setChanged()
    }

    override fun setChanged() {
        super.setChanged()
        slot.onContentsChanged()
    }

    override fun getMaxStackSize(): Int = slot.getLimit(ItemStack.EMPTY)

    override fun getMaxStackSize(stack: ItemStack): Int = slot.getLimit(stack)

    override fun mayPickup(player: Player): Boolean = !slot.extractItem(1, true, HTStorageAccess.MANUAL).isEmpty

    override fun remove(amount: Int): ItemStack = slot.extractItem(amount, false, HTStorageAccess.MANUAL)

    override fun tryRemove(count: Int, decrement: Int, player: Player): Optional<ItemStack> {
        if (allowPartialRemoval()) {
            if (!mayPickup(player)) {
                return Optional.empty()
            }
            val count: Int = min(count, decrement)
            val stack: ItemStack = remove(count)
            if (stack.isEmpty) {
                return Optional.empty()
            } else if (item.isEmpty) {
                setByPlayer(ItemStack.EMPTY, stack)
            }
            return Optional.of(stack)
        }

        return super.tryRemove(count, decrement, player)
    }

    protected open fun allowPartialRemoval(): Boolean = true
}
