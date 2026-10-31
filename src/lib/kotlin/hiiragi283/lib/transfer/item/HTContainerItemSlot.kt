package hiiragi283.lib.transfer.item

import hiiragi283.lib.transfer.HTSnapshotStackResourceSlot
import hiiragi283.lib.transfer.HTStackResourceHelper
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * [Container]に基づいた[HTSnapshotStackResourceSlot]の実装クラスです。
 *
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
class HTContainerItemSlot(val container: Container, val index: Int) :
    HTSnapshotStackResourceSlot<ItemStack, ItemResource>(),
    HTItemStackSlot {
    init {
        val maxSlot: Int = container.containerSize - 1
        require(index in (0..maxSlot)) { "Index: $index is out of max container slot index: $maxSlot" }
    }

    override fun getStackCopy(): ItemStack = container.getItem(index).copy()

    override fun setStack(stack: ItemStack) {
        container.setItem(index, stack)
    }

    override fun setStackInternal(stack: ItemStack) {
        container.setItem(index, stack)
    }

    override fun getHelper(): HTStackResourceHelper.Item = HTStackResourceHelper.Item

    override fun onRootCommit(originalState: ItemStack) {
        container.setChanged()
    }

    override fun isValid(resource: ItemResource): Boolean = container.canPlaceItem(index, resource.toStack())

    override fun getCapacity(resource: ItemResource): Int = container.getMaxStackSize(resource.toStack())

    operator fun component1(): Container = container

    operator fun component2(): Int = index
}
