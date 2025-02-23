package hiiragi283.ragium.common.capability.item

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable
import kotlin.math.min

class HTSingleItemVariantHandler(val capacity: Int, val callback: () -> Unit) : IItemHandlerModifiable {
    var stack: ItemStack = ItemStack.EMPTY
        private set
    var count: Int = 0
        private set

    fun encode(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        ItemStack.OPTIONAL_CODEC
            .encodeStart(dynamicOps, stack)
            .ifSuccess { nbt.put("stack", it) }
    }

    fun decode(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        ItemStack.OPTIONAL_CODEC
            .parse(dynamicOps, nbt.getCompound("stack"))
            .ifSuccess { stack = it }
    }

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        this.stack = stack.copyWithCount(1)
        count = stack.count
        callback()
    }

    override fun getSlots(): Int = 1

    override fun getStackInSlot(slot: Int): ItemStack = stack.copyWithCount(count)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty) return ItemStack.EMPTY
        if (!isItemValid(slot, stack)) return stack
        val count1: Int = stack.count
        if (ItemStack.isSameItemSameComponents(stack, this.stack) || stack.isEmpty) {
            val inserted: Int = min(count1, capacity - this.count)
            if (inserted > 0) {
                if (!simulate) {
                    this.stack = stack.copyWithCount(1)
                    this.count = inserted
                }
                return stack.copyWithCount(count1 - inserted)
            }
        }
        return stack
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (count == 0 || amount <= 0) return ItemStack.EMPTY
        val extracted: Int = min(amount, count)
        if (extracted > 0) {
            if (!simulate) {
                count -= extracted
                if (count <= 0) {
                    stack = ItemStack.EMPTY
                    count = 0
                }
            }
            return stack.copyWithCount(extracted)
        }
        return ItemStack.EMPTY
    }

    override fun getSlotLimit(slot: Int): Int = stack.maxStackSize

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = true
}
