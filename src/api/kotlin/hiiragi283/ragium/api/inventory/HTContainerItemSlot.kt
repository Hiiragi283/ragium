package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackSetter
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.Optional
import kotlin.math.min

/**
 * [HTItemSlot]に基づいた[Slot]の実装
 * @see mekanism.common.inventory.container.slot.InventoryContainerSlot
 */
open class HTContainerItemSlot(
    val slot: HTItemSlot,
    x: Int,
    y: Int,
    private val stackSetter: HTStackSetter<ImmutableItemStack>,
    private val manualFilter: (ImmutableItemStack, HTStorageAccess) -> Boolean,
    val slotType: Type,
) : Slot(emptyContainer, 0, x, y) {
    companion object {
        @JvmStatic
        private val emptyContainer = SimpleContainer(0)
    }

    override fun mayPlace(stack: ItemStack): Boolean {
        val immutable: ImmutableItemStack = stack.toImmutable() ?: return false
        if (slot.getStack() == null) {
            val remainder: Int = slot.insert(immutable, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL)?.amount() ?: 0
            return remainder < stack.count
        }
        if (slot.extract(1, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL) == null) return false
        return manualFilter(immutable, HTStorageAccess.MANUAL)
    }

    override fun getItem(): ItemStack = slot.getItemStack()

    override fun hasItem(): Boolean = slot.getStack() != null

    override fun set(stack: ItemStack) {
        stackSetter.setStack(stack.toImmutable())
        setChanged()
    }

    override fun setChanged() {
        super.setChanged()
        slot.onContentsChanged()
    }

    override fun getMaxStackSize(): Int = slot.getCapacity(null)

    override fun getMaxStackSize(stack: ItemStack): Int = slot.getNeeded(stack.toImmutable())

    override fun mayPickup(player: Player): Boolean = slot.extract(1, HTStorageAction.SIMULATE, HTStorageAccess.MANUAL) != null

    override fun remove(amount: Int): ItemStack =
        slot.extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)?.unwrap() ?: ItemStack.EMPTY

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

    enum class Type {
        INPUT,
        OUTPUT,
        BOTH,
    }
}
