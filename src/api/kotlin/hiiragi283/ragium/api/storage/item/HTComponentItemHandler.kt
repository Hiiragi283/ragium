package hiiragi283.ragium.api.storage.item

import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.common.MutableDataComponentHolder
import kotlin.math.max

/**
 * コンポーネントに基づいた[HTItemHandler]の抽象クラス
 * @see [net.neoforged.neoforge.items.ComponentItemHandler]
 */
abstract class HTComponentItemHandler(protected val parent: MutableDataComponentHolder, protected val size: Int) : HTItemHandler {
    abstract fun createSlot(slot: Int): HTItemSlot

    protected val slots: List<HTItemSlot> = (0 until size).map(::createSlot)
    protected val type: DataComponentType<ItemContainerContents> = DataComponents.CONTAINER
    protected val contents: ItemContainerContents get() = parent.getOrDefault(type, ItemContainerContents.EMPTY)

    protected open fun getStackForContents(contents: ItemContainerContents, slot: Int): ItemStack = when (slot) {
        in (0 until contents.slots) -> contents.getStackInSlot(slot)
        else -> ItemStack.EMPTY
    }

    protected open fun updateContents(contents: ItemContainerContents, stack: ItemStack, slot: Int) {
        val list: NonNullList<ItemStack> = NonNullList.withSize(max(contents.slots, size), ItemStack.EMPTY)
        contents.copyInto(list)
        // val oldStack: ItemStack = list[slot]
        list[slot] = stack
        parent.set(type, ItemContainerContents.fromItems(list))
        if (this.contents
                .nonEmptyStream()
                .toList()
                .isEmpty()
        ) {
            parent.remove(type)
        }
        onContentsChanged()
    }

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots

    override fun onContentsChanged() {}
}
