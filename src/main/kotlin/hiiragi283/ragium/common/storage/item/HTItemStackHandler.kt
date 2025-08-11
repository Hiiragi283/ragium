package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.function.Consumer

abstract class HTItemStackHandler(size: Int) :
    ItemStackHandler(size),
    HTItemHandler {
    companion object {
        @JvmField
        val EMPTY: HTItemStackHandler = object : HTItemStackHandler(0) {
            override fun onContentsChanged(slot: Int) {}

            override val inputSlots: IntArray = intArrayOf()
            override val outputSlots: IntArray = intArrayOf()
        }
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = when (slot) {
        !in inputSlots -> stack
        else -> super.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = when (slot) {
        !in outputSlots -> ItemStack.EMPTY
        else -> super<ItemStackHandler>.extractItem(slot, amount, simulate)
    }

    override fun onLoad() {
        slotRange.forEach(::onContentsChanged)
    }

    abstract override fun onContentsChanged(slot: Int)

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

        fun build(callback: Runnable): HTItemStackHandler = Simple(
            size,
            inputSlots.toIntArray(),
            outputSlots.toIntArray(),
        ) { callback.run() }

        fun build(callback: Consumer<Int>): HTItemStackHandler = Simple(size, inputSlots.toIntArray(), outputSlots.toIntArray(), callback)
    }

    //    Simple    //

    private class Simple(
        size: Int,
        override val inputSlots: IntArray,
        override val outputSlots: IntArray,
        private val callback: Consumer<Int>,
    ) : HTItemStackHandler(size) {
        override fun onContentsChanged(slot: Int) {
            callback.accept(slot)
        }
    }
}
