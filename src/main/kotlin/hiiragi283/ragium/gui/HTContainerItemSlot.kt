package hiiragi283.ragium.gui

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.item.toResourcePair
import hiiragi283.lib.transfer.useTransaction
import java.util.Optional
import java.util.function.BiPredicate
import java.util.function.Consumer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.world.inventory.StackCopySlot

class HTContainerItemSlot(
    val slot: HTItemSlot,
    index: Int,
    x: Int,
    y: Int,
    private val stackSetter: Consumer<ItemStack>,
    private val manualFilter: BiPredicate<ItemResource, HTTransferAccess>,
    val slotType: HTBackgroundType,
) : StackCopySlot(index, x, y) {
    companion object {
        @JvmStatic
        fun create(
            slot: HTBasicItemSlot,
            index: Int,
            x: Int,
            y: Int,
            slotType: HTBackgroundType,
        ): HTContainerItemSlot = HTContainerItemSlot(
            slot,
            index,
            x,
            y,
            slot::stack::set,
            slot::isStackValidForInsert,
            slotType,
        )
    }

    override fun getStackCopy(): ItemStack = slot.getItemStack()

    override fun setStackCopy(stack: ItemStack) {
        stackSetter.accept(stack)
    }

    override fun mayPlace(itemStack: ItemStack): Boolean {
        val (resource: ItemResource, count: Int) = itemStack.toResourcePair()
        if (slot.resource.isEmpty) {
            return useTransaction { slot.insert(resource, count, it, HTTransferAccess.MANUAL) } < count
        }
        if (useTransaction { slot.extractSelf(1, it, HTTransferAccess.MANUAL) } == 0) return false
        return manualFilter.test(resource, HTTransferAccess.MANUAL)
    }

    override fun getMaxStackSize(): Int = slot.currentCapacity

    override fun getMaxStackSize(itemStack: ItemStack): Int = slot.getCapacity(ItemResource.of(itemStack))

    override fun mayPickup(player: Player): Boolean = useTransaction { slot.extractSelf(1, it, HTTransferAccess.MANUAL) } > 0

    override fun tryRemove(amount: Int, maxAmount: Int, player: Player): Optional<ItemStack> {
        if (!mayPickup(player)) {
            return Optional.empty()
        }
        val count: Int = minOf(amount, maxAmount)
        val stack: ItemStack = remove(count)
        if (stack.isEmpty) {
            return Optional.empty()
        } else if (item.isEmpty) {
            setByPlayer(ItemStack.EMPTY, stack)
        }
        return Optional.of(stack)
    }

    override fun toString(): String = "HTContainerItemSlot(slot=$slot, x=$x, y=$y, slotType=$slotType)"
}
