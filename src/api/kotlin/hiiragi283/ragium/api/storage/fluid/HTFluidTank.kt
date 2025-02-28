package hiiragi283.ragium.api.storage.fluid

import com.google.common.util.concurrent.Runnables
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.extension.constFunction2
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageListener
import hiiragi283.ragium.api.util.HTNbtCodec
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.IFluidTank
import kotlin.math.min

/**
 * @see [mekanism.api.fluid.IExtendedFluidTank]
 */
interface HTFluidTank :
    IFluidTank,
    HTStorageListener,
    HTNbtCodec {
    fun setFluid(stack: FluidStack)

    override fun getFluidAmount(): Int = fluid.amount

    fun canFill(stack: FluidStack): Boolean

    /**
     * @return 更新された[FluidStack]の個数
     */
    fun setStackSize(amount: Int, simulate: Boolean): Int {
        if (isEmpty()) return 0
        if (amount <= 0) {
            if (!simulate) {
                removeStack()
            }
            return 0
        }
        val stack: FluidStack = fluid
        val fixedAmount: Int = min(amount, capacity)
        if (stack.amount == fixedAmount || simulate) {
            return fixedAmount
        }
        setFluid(stack.copyWithAmount(fixedAmount))
        return fixedAmount
    }

    /**
     * @return 実際に増加した個数
     */
    fun growStack(amount: Int, simulate: Boolean): Int {
        val stack: FluidStack = fluid
        val current: Int = stack.amount
        if (current == 0) {
            return 0
        }
        val fixedAmount: Int = min(amount, capacity)
        val newSize: Int = setStackSize(current + fixedAmount, simulate)
        return newSize - current
    }

    /**
     * @return 実際に減少した個数
     */
    fun shrinkStack(amount: Int, simulate: Boolean): Int = growStack(-amount, simulate)

    fun canShrink(amount: Int): Boolean = shrinkStack(amount, true) == -amount

    fun canShrink(amount: Int, simulate: Boolean): Boolean = shrinkStack(amount, simulate) == amount

    fun isEmpty(): Boolean = fluid.isEmpty

    fun removeStack() {
        setFluid(FluidStack.EMPTY)
    }

    fun updateCapacity(blockEntity: HTEnchantableBlockEntity)

    fun interactWithFluidStorage(player: Player, storageIO: HTStorageIO): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, storageIO.wrapFluidTank(this))

    //    Builder    //

    class Builder {
        private var capacity: Int = 8000
        private var validator: (FluidStack) -> Boolean = constFunction2(true)
        private var callback: Runnable = Runnables.doNothing()

        fun setCapacity(capacity: Int): Builder = apply {
            this.capacity = capacity
        }

        fun setValidator(validator: (FluidStack) -> Boolean): Builder = apply {
            this.validator = validator
        }

        fun setCallback(callback: Runnable): Builder = apply {
            this.callback = callback
        }

        fun build(nbtKey: String): HTFluidTank = RagiumAPI.getInstance().buildFluidTank(nbtKey, capacity, validator, callback)
    }
}
