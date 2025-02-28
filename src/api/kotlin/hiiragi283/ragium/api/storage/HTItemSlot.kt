package hiiragi283.ragium.api.storage

import com.google.common.util.concurrent.Runnables
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.constFunction2
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.util.HTNbtCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.min

/**
 * @see [mekanism.api.inventory.IInventorySlot]
 */
interface HTItemSlot :
    HTSlotListener,
    HTNbtCodec {
    fun getStack(): ItemStack

    fun setStack(stack: ItemStack)

    fun canInsert(stack: ItemStack): Boolean

    fun insertItem(stack: ItemStack, simulate: Boolean): ItemStack

    fun extractItem(amount: Int, simulate: Boolean): ItemStack

    fun getMaxSize(stack: ItemStack): Int

    fun isItemValid(stack: ItemStack): Boolean

    fun createContainerSlot(x: Int, y: Int): Slot = createContainerSlot(x, y, HTStorageIO.GENERIC)

    fun createContainerSlot(x: Int, y: Int, storageIO: HTStorageIO): Slot

    /**
     * @return 更新された[ItemStack]の個数
     */
    fun setStackSize(amount: Int, simulate: Boolean): Int {
        if (isEmpty()) return 0
        if (amount <= 0) {
            if (!simulate) {
                removeStack()
            }
            return 0
        }
        val stack: ItemStack = getStack()
        val fixedAmount: Int = min(amount, getMaxSize(stack))
        if (stack.count == fixedAmount || simulate) {
            return fixedAmount
        }
        setStack(stack.copyWithCount(fixedAmount))
        return fixedAmount
    }

    /**
     * @return 実際に増加した個数
     */
    fun growStack(amount: Int, simulate: Boolean): Int {
        val stack: ItemStack = getStack()
        val current: Int = stack.count
        if (current == 0) {
            return 0
        }
        val fixedAmount: Int = min(amount, getMaxSize(stack))
        val newSize: Int = setStackSize(current + fixedAmount, simulate)
        return newSize - current
    }

    /**
     * @return 実際に減少した個数
     */
    fun shrinkStack(amount: Int, simulate: Boolean): Int = growStack(-amount, simulate)

    fun canShrink(amount: Int): Boolean = shrinkStack(amount, true) == -amount

    fun canShrink(amount: Int, simulate: Boolean): Boolean = shrinkStack(amount, simulate) == -amount

    fun isEmpty(): Boolean = getStack().isEmpty

    fun removeStack() {
        setStack(ItemStack.EMPTY)
    }

    fun dropStack(entity: Entity) {
        dropStackAt(entity, getStack())
        removeStack()
    }

    fun dropStack(level: Level, pos: BlockPos) {
        dropStackAt(level, pos, getStack())
        removeStack()
    }

    //    Builder    //

    class Builder {
        private var maxSize: Int = Item.ABSOLUTE_MAX_STACK_SIZE
        private var validator: (ItemStack) -> Boolean = constFunction2(true)
        private var callback: Runnable = Runnables.doNothing()

        fun setMaxSize(maxSize: Int): Builder = apply {
            this.maxSize = maxSize
        }

        fun setValidator(validator: (ItemStack) -> Boolean): Builder = apply {
            this.validator = validator
        }

        fun setCallback(callback: Runnable): Builder = apply {
            this.callback = callback
        }

        fun build(nbtKey: String): HTItemSlot = RagiumAPI.getInstance().buildItemSlot(
            nbtKey,
            maxSize,
            validator,
            callback,
        )
    }
}
