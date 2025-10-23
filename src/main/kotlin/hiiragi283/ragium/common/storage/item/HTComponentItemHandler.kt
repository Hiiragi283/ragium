package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.item.asNonEmptySequence
import hiiragi283.ragium.api.item.copy
import hiiragi283.ragium.api.item.getOrEmpty
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents

/**
 * [HTItemHandler]に基づいたコンポーネント向けの実装
 */
open class HTComponentItemHandler(protected val parent: ItemStack, protected val size: Int) : HTItemHandler {
    protected val slots: List<HTItemSlot> = (0..<size).map(::createSlot)

    protected open fun createSlot(slot: Int): HTItemSlot = ComponentSlot(parent, size, slot)

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots

    protected open class ComponentSlot(protected val parent: ItemStack, protected val size: Int, protected val slot: Int) :
        HTItemSlot.Mutable(),
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        protected val component: DataComponentType<ItemContainerContents> get() = DataComponents.CONTAINER

        protected fun getContents(): ItemContainerContents = parent.getOrDefault(component, ItemContainerContents.EMPTY)

        override fun isValid(stack: ImmutableItemStack): Boolean = stack.stack.canFitInsideContainerItems()

        override fun getStack(): ImmutableItemStack = getContents().getOrEmpty(slot).toImmutable()

        override fun getCapacityAsInt(stack: ImmutableItemStack): Int = HTItemSlot.getMaxStackSize(stack)

        override fun setStack(stack: ImmutableItemStack) {
            val contents: ItemContainerContents = getContents().copy(size) { set(slot, stack.stack) }
            parent.setOrRemove(component, contents) { it.asNonEmptySequence().none() }
            onContentsChanged()
        }
    }
}
