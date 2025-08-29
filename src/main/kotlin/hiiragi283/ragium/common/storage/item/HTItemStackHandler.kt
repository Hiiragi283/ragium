package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import kotlin.math.min

abstract class HTItemStackHandler : HTItemHandler {
    companion object {
        @JvmField
        val EMPTY: HTItemStackHandler = Builder(0).build()

        @JvmStatic
        fun codec(factory: (List<ItemStack>) -> HTItemStackHandler): BiCodec<RegistryFriendlyByteBuf, HTItemStackHandler> = BiCodecs
            .itemStack(true)
            .listOf()
            .xmap(factory, HTItemStackHandler::stacks)
    }

    protected var stacks: MutableList<ItemStack>
        private set

    constructor(stacks: MutableList<ItemStack>) {
        this.stacks = stacks
    }

    constructor(size: Int) {
        this.stacks = MutableList(size) { ItemStack.EMPTY }
    }

    override val isEmpty: Boolean get() = stacks.isEmpty() || stacks.all(ItemStack::isEmpty)

    override fun getStackView(): Iterable<ItemStack> = stacks.map(ItemStack::copy)

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        validateSlotIndex(slot)
        stacks[slot] = stack
        onContentsChanced(slot)
    }

    override fun getSlots(): Int = stacks.size

    override fun getStackInSlot(slot: Int): ItemStack {
        validateSlotIndex(slot)
        return stacks[slot]
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty) return ItemStack.EMPTY
        if (!isItemValid(slot, stack)) return stack

        val existing: ItemStack = getStackInSlot(slot)
        var limit: Int = getStackLimit(slot, stack)

        if (!existing.isEmpty) {
            if (ItemStack.isSameItemSameComponents(stack, existing)) return stack
            limit -= existing.count
        }
        if (limit <= 0) return stack
        val reachedLimit: Boolean = stack.count > limit

        if (!simulate) {
            if (existing.isEmpty) {
                stacks[slot] = if (reachedLimit) stack.copyWithCount(limit) else stack
            } else {
                existing.grow(if (reachedLimit) limit else stack.count)
            }
            onContentsChanced(slot)
        }
        return when (reachedLimit) {
            true -> stack.copyWithCount(stack.count - limit)
            false -> ItemStack.EMPTY
        }
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (amount == 0) return ItemStack.EMPTY
        val existing: ItemStack = getStackInSlot(slot)
        if (existing.isEmpty) return ItemStack.EMPTY

        val toExtract: Int = min(amount, existing.maxStackSize)
        if (existing.count <= toExtract) {
            if (!simulate) {
                stacks[slot] = ItemStack.EMPTY
                onContentsChanced(slot)
                return existing
            } else {
                return existing.copy()
            }
        } else {
            if (!simulate) {
                stacks[slot] = existing.copyWithCount(existing.count - toExtract)
                onContentsChanced(slot)
            }
        }
        return existing.copyWithCount(toExtract)
    }

    override fun getSlotLimit(slot: Int): Int = Item.ABSOLUTE_MAX_STACK_SIZE

    protected open fun getStackLimit(slot: Int, stack: ItemStack): Int = min(getSlotLimit(slot), stack.maxStackSize)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = true

    final override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
        val list = ListTag()
        stacks.forEachIndexed { slot: Int, stack: ItemStack ->
            if (!stack.isEmpty) {
                list.add(
                    buildNbt {
                        putInt("Slot", slot)
                        stack.save(provider, this)
                    },
                )
            }
        }
        return buildNbt {
            put("Items", list)
            putInt("Size", stacks.size)
        }
    }

    final override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        if (nbt.contains("Size")) {
            stacks = MutableList(nbt.getInt("Size")) { ItemStack.EMPTY }
        }
        val list: ListTag = nbt.getList("Items", 10)
        for (tag: Tag in list) {
            if (tag is CompoundTag) {
                val slot: Int = tag.getInt("Slot")
                if (slot in (0 until stacks.size)) {
                    ItemStack.parse(provider, tag).ifPresent { stack: ItemStack -> stacks[slot] = stack }
                }
            }
        }
        onLoad()
    }

    protected fun validateSlotIndex(slot: Int) {
        check(slot in (0 until stacks.size)) {
            "Slot $slot not in valid range - [0, ${stacks.size})"
        }
    }

    protected open fun onLoad() {
        slotRange.forEach(::onContentsChanced)
    }

    protected open fun onContentsChanced(slot: Int) {
        onContentsChanged()
    }

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

        fun build(): HTItemStackHandler = build(null)

        fun build(callback: HTContentListener?): HTItemStackHandler =
            Simple(size, inputSlots.toIntArray(), outputSlots.toIntArray(), callback)
    }

    //    Simple    //

    private class Simple(
        size: Int,
        override val inputSlots: IntArray,
        override val outputSlots: IntArray,
        private val callback: HTContentListener?
    ) : HTItemStackHandler(size) {
        override fun onContentsChanged() {
            callback?.onContentsChanged()
        }
    }
}
