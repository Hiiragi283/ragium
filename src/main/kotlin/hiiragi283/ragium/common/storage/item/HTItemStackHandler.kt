package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler

abstract class HTItemStackHandler(size: Int) :
    ItemStackHandler(size),
    HTItemHandler {
    companion object {
        @JvmField
        val EMPTY: HTItemStackHandler = object : HTItemStackHandler(0) {
            override fun onContentsChanged() {}

            override val inputSlots: IntArray = intArrayOf()
            override val outputSlots: IntArray = intArrayOf()
        }
    }

    override fun onLoad() {
        slotRange.forEach(::onContentsChanged)
    }

    override fun onContentsChanged(slot: Int) {
        onContentsChanged()
    }

    //    Extensions    //

    override val isEmpty: Boolean get() = stacks.isEmpty() || stacks.all(ItemStack::isEmpty)

    override fun getStackView(): Iterable<ItemStack> = stacks.map(ItemStack::copy)

    //    Builder    //

    class Builder(private val size: Int) {
        private val inputSlots: MutableList<Int> = mutableListOf()
        private val outputSlots: MutableList<Int> = mutableListOf()

        fun addInput(vararg slots: Int): Builder = apply {
            inputSlots.addAll(slots.toTypedArray())
        }

        fun addInput(slots: IntRange): Builder = apply {
            inputSlots.addAll(slots)
        }

        fun addOutput(vararg slots: Int): Builder = apply {
            outputSlots.addAll(slots.toTypedArray())
        }

        fun addOutput(slots: IntRange): Builder = apply {
            outputSlots.addAll(slots)
        }

        fun build(callback: HTContentListener): HTItemStackHandler =
            Simple(size, inputSlots.toIntArray(), outputSlots.toIntArray(), callback)
    }

    //    Simple    //

    private class Simple(
        size: Int,
        override val inputSlots: IntArray,
        override val outputSlots: IntArray,
        private val callback: HTContentListener,
    ) : HTItemStackHandler(size),
        HTContentListener by callback
}
