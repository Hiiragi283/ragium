package hiiragi283.ragium.common.storage.item

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.extension.asNonEmptySequence
import hiiragi283.ragium.api.extension.copy
import hiiragi283.ragium.api.extension.getOrEmpty
import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.value.HTValueInput
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
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

    override fun onContentsChanged() {}

    protected open class ComponentSlot(protected val parent: MutableDataComponentHolder, protected val size: Int, protected val slot: Int) :
        HTItemSlot.Mutable {
        protected val component: DataComponentType<ItemContainerContents> get() = DataComponents.CONTAINER

        protected fun getContents(): ItemContainerContents = parent.getOrDefault(component, ItemContainerContents.EMPTY)

        override fun createContainerSlot(): Slot? = null

        override fun getStack(): ItemStack = getContents().getOrEmpty(slot)

        override fun getCapacityAsLong(stack: ItemStack): Long = HTItemSlot.ABSOLUTE_MAX_STACK_SIZE

        override fun isValid(stack: ItemStack): Boolean = true

        override fun deserialize(input: HTValueInput) {}

        override fun onContentsChanged() {}

        override fun setStack(stack: ItemStack) {
            val contents: ItemContainerContents = getContents().copy(size) { set(slot, stack) }
            parent.setOrRemove(component, contents) { it.asNonEmptySequence().any() }
            onContentsChanged()
        }
    }
}
