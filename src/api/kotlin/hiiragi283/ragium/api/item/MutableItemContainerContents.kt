package hiiragi283.ragium.api.item

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents

class MutableItemContainerContents private constructor(private val items: MutableList<ItemStack>) : Iterable<ItemStack> {
    companion object {
        @JvmStatic
        fun of(content: ItemContainerContents): MutableItemContainerContents =
            MutableItemContainerContents(content.stream().toList().toMutableList())
    }

    fun getItem(slot: Int): ItemStack = items.getOrNull(slot) ?: ItemStack.EMPTY

    fun setItem(slot: Int, stack: ItemStack) {
        items[slot] = stack
    }

    fun toImmutable(): ItemContainerContents = ItemContainerContents.fromItems(items)

    fun getSlotRange(): IntRange = items.indices

    override fun iterator(): Iterator<ItemStack> = items.iterator()
}
