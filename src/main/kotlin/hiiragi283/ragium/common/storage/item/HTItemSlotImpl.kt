package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.inventory.HTContainerItemSlot
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import kotlin.math.min

class HTItemSlotImpl(
    private val nbtKey: String,
    private val maxSize: Int,
    private val validator: (ItemStack) -> Boolean,
    private val callback: Runnable,
) : HTItemSlot {
    private var stackIn: ItemStack = ItemStack.EMPTY

    //    HTItemSlot    //

    override fun getStack(): ItemStack = stackIn

    override fun setStack(stack: ItemStack) {
        this.stackIn = stack
    }

    override fun getMaxSize(stack: ItemStack): Int = min(stack.maxStackSize, maxSize)

    override fun canInsert(stack: ItemStack): Boolean = validator(stack)

    override fun insertItem(stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty) return ItemStack.EMPTY
        val required: Int = getMaxSize(stack) - stackIn.count
        if (required <= 0 || !canInsert(stack)) return stack
        val isSameItem: Boolean = ItemStack.isSameItemSameComponents(stack, stackIn)
        if (stackIn.isEmpty || isSameItem) {
            val countAdd: Int = min(stack.count, required)
            if (!simulate) {
                if (isSameItem) {
                    stackIn.grow(countAdd)
                    onContentsChanged()
                } else {
                    setStack(stack.copyWithCount(countAdd))
                }
            }
            return stack.copyWithCount(stack.count - countAdd)
        }
        return stack
    }

    override fun extractItem(amount: Int, simulate: Boolean): ItemStack {
        if (stackIn.isEmpty) return ItemStack.EMPTY
        if (amount <= 0) return ItemStack.EMPTY
        // if (!storageIO.canExtract(stackIn)) return ItemStack.EMPTY
        val actualCount: Int = min(stackIn.count, stackIn.maxStackSize)
        val fixedAmount: Int = min(amount, actualCount)
        val result: ItemStack = stackIn.copyWithCount(fixedAmount)
        if (!simulate) {
            stackIn.shrink(fixedAmount)
            onContentsChanged()
        }
        return result
    }

    override fun isItemValid(stack: ItemStack): Boolean = validator(stack)

    override fun createContainerSlot(x: Int, y: Int, storageIO: HTStorageIO): Slot = HTContainerItemSlot(
        this,
        storageIO,
        HTSlotPos.getSlotPosX(x),
        HTSlotPos.getSlotPosY(y),
    )

    override fun onContentsChanged() {
        callback.run()
    }

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemStack.OPTIONAL_CODEC
            .encodeStart(registryOps, stackIn)
            .ifSuccess { nbt.put(nbtKey, it) }
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemStack.OPTIONAL_CODEC
            .parse(registryOps, nbt.get(nbtKey))
            .ifSuccess(::setStack)
    }
}
