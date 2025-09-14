package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.inventory.slot.HTContainerItemSlot
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.value.HTValueInput
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.common.MutableDataComponentHolder
import kotlin.math.max

/**
 * [HTItemHandler]に基づいたコンポーネント向けの実装
 */
open class HTComponentItemHandler(protected val parent: MutableDataComponentHolder, protected val size: Int) : HTItemHandler {
    protected val slots: List<HTItemSlot> = (0..<size).map(::createSlot)

    protected open fun createSlot(slot: Int): HTItemSlot = ComponentSlot(parent, size, slot)

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots

    override fun onContentsChanged() {}

    protected open class ComponentSlot(protected val parent: MutableDataComponentHolder, protected val size: Int, protected val slot: Int) :
        HTItemSlot {
        protected val component: DataComponentType<ItemContainerContents> get() = DataComponents.CONTAINER

        protected fun getContents(): ItemContainerContents = parent.getOrDefault(component, ItemContainerContents.EMPTY)

        override fun getStack(): ItemStack = when (slot) {
            in (0..<getContents().slots) -> getContents().getStackInSlot(slot)
            else -> ItemStack.EMPTY
        }

        override fun setStack(stack: ItemStack) {
            var contents: ItemContainerContents = getContents()
            val list: NonNullList<ItemStack> = NonNullList.withSize(max(contents.slots, size), ItemStack.EMPTY)
            contents.copyInto(list)
            // val oldStack: ItemStack = list[slot]
            list[slot] = stack
            contents = ItemContainerContents.fromItems(list)
            parent.setOrRemove(component, contents) { it.nonEmptyStream().findAny().isEmpty }
            onContentsChanged()
        }

        override fun getLimit(stack: ItemStack): Int = Item.ABSOLUTE_MAX_STACK_SIZE

        override fun isItemValid(stack: ItemStack): Boolean = true

        override fun createContainerSlot(): Slot? = HTContainerItemSlot(
            this,
            HTSlotHelper.getSlotPosX(slot % 9),
            HTSlotHelper.getSlotPosY(slot / 9),
            ::setStack,
        )

        override fun deserialize(input: HTValueInput) {}

        override fun onContentsChanged() {}
    }
}
