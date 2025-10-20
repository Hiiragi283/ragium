package hiiragi283.ragium.common.storage.item

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.RagiumConst
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
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.common.MutableDataComponentHolder

/**
 * [HTItemHandler]に基づいたコンポーネント向けの実装
 */
open class HTComponentItemHandler(protected val parent: MutableDataComponentHolder, protected val size: Int) : HTItemHandler {
    constructor(parent: MutableDataComponentHolder, size: Long) : this(parent, Ints.saturatedCast(size))

    protected val slots: List<HTItemSlot> = (0..<size).map(::createSlot)

    protected open fun createSlot(slot: Int): HTItemSlot = ComponentSlot(parent, size, slot)

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots

    protected open class ComponentSlot(protected val parent: MutableDataComponentHolder, protected val size: Int, protected val slot: Int) :
        HTItemSlot.Mutable(),
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        protected val component: DataComponentType<ItemContainerContents> get() = DataComponents.CONTAINER

        protected fun getContents(): ItemContainerContents = parent.getOrDefault(component, ItemContainerContents.EMPTY)

        override fun getStack(): ImmutableItemStack = getContents().getOrEmpty(slot).toImmutable()

        override fun getCapacityAsLong(stack: ImmutableItemStack): Long = RagiumConst.ABSOLUTE_MAX_STACK_SIZE

        override fun isValid(stack: ImmutableItemStack): Boolean = stack.stack.canFitInsideContainerItems()

        override fun setStack(stack: ImmutableItemStack) {
            val contents: ItemContainerContents = getContents().copy(size) { set(slot, stack.stack) }
            parent.setOrRemove(component, contents) { it.asNonEmptySequence().any() }
            onContentsChanged()
        }
    }
}
