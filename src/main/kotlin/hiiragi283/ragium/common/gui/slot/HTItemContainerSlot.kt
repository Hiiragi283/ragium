package hiiragi283.ragium.common.gui.slot

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.HTItemStackSlot
import hiiragi283.lib.transfer.item.getItemStack
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import net.neoforged.neoforge.world.inventory.StackCopySlot
import java.util.Optional
import java.util.function.Consumer

/**
 * [HTItemSlot]に基づいた[StackCopySlot]の実装クラスです。
 *
 * 参照 : [Mekanism - InventoryContainerSlot](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/inventory/container/slot/InventoryContainerSlot.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
open class HTItemContainerSlot(val slot: HTItemSlot, x: Int, y: Int, private val stackSetter: Consumer<ItemStack>) :
    StackCopySlot(0, x, y),
    HTTransactionSlot {
    companion object {
        @JvmStatic
        fun create(slot: HTItemStackSlot, x: Int, y: Int): HTItemContainerSlot = HTItemContainerSlot(slot, x, y, slot::setStack)
    }

    override fun insert(resource: ItemResource, amount: Int, transaction: TransactionContext): Int =
        if (resource.isEmpty) 0 else slot.insert(resource, amount, transaction, HTTransferAccess.MANUAL)

    override fun extract(player: Player, resource: ItemResource, amount: Int, transaction: TransactionContext): Int =
        if (resource.isEmpty) 0 else slot.extract(resource, amount, transaction, HTTransferAccess.MANUAL)

    override fun mayPlace(itemStack: ItemStack): Boolean {
        if (itemStack.isEmpty) return false
        val resourceIn: ItemResource = slot.resource
        val resource: ItemResource = ItemResource.of(itemStack)
        return when {
            // スロット内のリソースと設置するリソースが異なる場合，スロットから取り出せるかどうかで判定
            !resourceIn.isEmpty && resourceIn != resource && !slot.canResourceExtract(resourceIn, HTTransferAccess.MANUAL) -> false

            // スロットが空または同じリソースの場合は，搬入できるかどうかで判定
            else -> slot.isValidForInsert(resource, HTTransferAccess.MANUAL)
        }
    }

    override fun getStackCopy(): ItemStack = slot.getItemStack()

    override fun setStackCopy(stack: ItemStack) {
        stackSetter.accept(stack)
    }

    override fun getMaxStackSize(): Int = slot.getCapacity(ItemResource.EMPTY)

    override fun getMaxStackSize(itemStack: ItemStack): Int = slot.getCapacity(ItemResource.of(itemStack))

    override fun mayPickup(player: Player): Boolean = !slot.isEmpty && slot.canResourceExtract(slot.resource, HTTransferAccess.MANUAL)

    override fun isSameInventory(other: Slot): Boolean = other is HTItemContainerSlot && other.slot == this.slot

    override fun tryRemove(amount: Int, maxAmount: Int, player: Player): Optional<ItemStack> {
        if (!mayPickup(player)) {
            return Optional.empty()
        }
        val amount1: Int = minOf(amount, maxAmount)
        val result: ItemStack = remove(amount1)
        if (result.isEmpty) {
            return Optional.empty()
        } else if (item.isEmpty) {
            setByPlayer(ItemStack.EMPTY, result)
        }
        return Optional.of(result)
    }
}
