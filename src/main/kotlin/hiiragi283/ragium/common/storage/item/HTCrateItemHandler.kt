package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.ItemStack

class HTCrateItemHandler(private val multiplier: Int, parent: ItemStack) : HTComponentItemHandler(parent, 1) {
    override fun createSlot(slot: Int): HTItemSlot = CrateSlot(multiplier, parent)

    private class CrateSlot(private val multiplier: Int, private val parent: ItemStack) :
        HTItemSlot.Mutable(),
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        override fun isValid(stack: ImmutableItemStack): Boolean = stack.stack.canFitInsideContainerItems()

        override fun getStack(): ImmutableItemStack = parent.getOrDefault(RagiumDataComponents.ITEM_CONTENT, ImmutableItemStack.EMPTY)

        override fun getCapacityAsInt(stack: ImmutableItemStack): Int {
            val capacity: Int = HTItemSlot.getMaxStackSize(stack) * multiplier
            return HTItemHelper.processStorageCapacity(null, parent, capacity)
        }

        override fun setStack(stack: ImmutableItemStack) {
            parent.setOrRemove(RagiumDataComponents.ITEM_CONTENT, stack, ImmutableItemStack::isEmpty)
            onContentsChanged()
        }
    }
}
