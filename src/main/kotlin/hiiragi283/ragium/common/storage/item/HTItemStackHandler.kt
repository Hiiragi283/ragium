package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler

abstract class HTItemStackHandler :
    ItemStackHandler,
    HTItemHandler {
    companion object {
        @JvmField
        val EMPTY: HTItemStackHandler = object : HTItemStackHandler(0) {
            override fun onContentsChanged() {}

            override val inputSlots: IntArray = intArrayOf()
            override val outputSlots: IntArray = intArrayOf()
        }

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemStackHandler> = BiCodecs
            .itemStack(true)
            .nonNullList()
            .xmap({ stacks: NonNullList<ItemStack> ->
                Simple(
                    stacks,
                    intArrayOf(),
                    intArrayOf(),
                    HTContentListener.NONE,
                )
            }, HTItemStackHandler::stacks)
    }

    constructor(size: Int = 1) : super(size)

    constructor(stacks: NonNullList<ItemStack>) : super(stacks)

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

    private class Simple : HTItemStackHandler {
        constructor(size: Int, inputSlots: IntArray, outputSlots: IntArray, callback: HTContentListener) : super(size) {
            this.inputSlots = inputSlots
            this.outputSlots = outputSlots
            this.callback = callback
        }

        constructor(
            stacks: NonNullList<ItemStack>,
            inputSlots: IntArray,
            outputSlots: IntArray,
            callback: HTContentListener,
        ) : super(stacks) {
            this.inputSlots = inputSlots
            this.outputSlots = outputSlots
            this.callback = callback
        }

        override val inputSlots: IntArray
        override val outputSlots: IntArray
        private val callback: HTContentListener

        override fun onContentsChanged() {
            callback.onContentsChanged()
        }
    }
}
