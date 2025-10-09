package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * @see [mekanism.common.inventory.slot.IFluidHandlerSlot]
 */
interface HTFluidItemSlot : HTItemSlot.Mutable {
    fun getFluidTank(): HTFluidTank

    var isDraining: Boolean
    var isFilling: Boolean

    fun handleTank(slot: HTItemSlot.Mutable, mode: Boolean) {
        if (!isEmpty()) {
            when (mode) {
                true -> fillTank(slot)
                false -> drainTank(slot)
            }
        }
    }

    fun fillTank(slot: HTItemSlot.Mutable) {
        val handler: IFluidHandlerItem = HTMultiCapability.FLUID.getCapability(getStack()) ?: return
        val tanks: Int = handler.tanks
        when {
            tanks == 1 -> {
                val stackIn: FluidStack = handler.getFluidInTank(0)
                if (!stackIn.isEmpty && getFluidTank().isValid(stackIn)) {
                    drainItemAndMove(slot, stackIn)
                }
            }

            else -> return
        }
    }

    fun drainTank(slot: HTItemSlot.Mutable) {
        if (!HTMultiCapability.FLUID.hasCapability(getStack())) return
        val stackIn: FluidStack = getFluidTank().getStack()
        if (!stackIn.isEmpty) {
            val simulatedDrain: FluidStack = getFluidTank().extract(stackIn.amount, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAl)
            if (simulatedDrain.isEmpty) return
        }
        val itemCopied: ItemStack = getStack().copyWithCount(1)
        val handler: IFluidHandlerItem = HTMultiCapability.FLUID.getCapability(itemCopied) ?: return
        val toDrain: Int = handler.fill(stackIn.copy(), IFluidHandler.FluidAction.EXECUTE)
        if (toDrain == 0) return
        if (this.getAmountAsInt() == 1) {
            val handler1: IFluidHandlerItem = HTMultiCapability.FLUID.getCapability(handler.container) ?: return
            if (handler1.fill(stackIn.copy(), IFluidHandler.FluidAction.SIMULATE) > 0) {
                setStack(handler.container)
                isDraining = true
                getFluidTank().extract(toDrain, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAl)
                return
            }
        }
    }

    private fun drainItemAndMove(slot: HTItemSlot.Mutable, fluidStack: FluidStack): Boolean {
        val remainder: FluidStack = getFluidTank().insert(fluidStack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAl)
        val remainderAmount: Int = remainder.amount
        val toTransfer: Int = fluidStack.amount
        if (remainderAmount == toTransfer) return false

        val stack: ItemStack = getStack()
        if (!HTMultiCapability.FLUID.hasCapability(stack)) return false

        val input: ItemStack = stack.copyWithCount(1)
        val handler: IFluidHandlerItem = HTMultiCapability.FLUID.getCapability(input) ?: return false
        val drained: FluidStack =
            handler.drain(fluidStack.copyWithAmount(toTransfer - remainderAmount), IFluidHandler.FluidAction.EXECUTE)
        if (drained.isEmpty) return false

        if (this.getAmountAsInt() == 1) {
            HTMultiCapability.FLUID.getCapability(handler.container)?.let { handler1: IFluidHandlerItem ->
                if (!handler1.drain(Int.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).isEmpty) {
                    setStack(handler1.container)
                    getFluidTank().insert(drained, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAl)
                    isFilling = true
                    return true
                }
            }
        }

        if (moveItem(slot, handler.container)) {
            getFluidTank().insert(drained, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAl)
            return true
        }
        return false
    }

    private fun moveItem(slot: HTItemSlot.Mutable, itemStack: ItemStack): Boolean {
        if (slot.isEmpty()) {
            slot.setStack(itemStack)
        } else {
            val stackIn: ItemStack = slot.getStack()
            if (!ItemStack.isSameItemSameComponents(stackIn, itemStack)) return false
            if (stackIn.count >= slot.getCapacityAsInt(stackIn)) return false
            slot.growStack(1, HTStorageAction.EXECUTE)
        }
        shrinkStack(1, HTStorageAction.EXECUTE)
        return true
    }

    fun fillTank(): Boolean {
        if (this.getAmountAsInt() != 1) return false
        val handler: IFluidHandlerItem = HTMultiCapability.FLUID.getCapability(getStack()) ?: return false
        if (handler.tanks != 1) return false
        val stackIn: FluidStack = handler.getFluidInTank(0)
        if (!stackIn.isEmpty && getFluidTank().isValid(stackIn)) {
            if (fillHandlerFromOther(getFluidTank(), handler, stackIn)) {
                setStack(handler.container)
                return true
            }
        }
        return false
    }

    private fun fillHandlerFromOther(toFill: HTFluidTank, toDrain: IFluidHandler, stack: FluidStack): Boolean {
        val simulatedDrain: FluidStack = toDrain.drain(stack.copy(), IFluidHandler.FluidAction.SIMULATE)
        if (simulatedDrain.isEmpty) return false
        val remainder: FluidStack = getFluidTank().insert(simulatedDrain, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAl)
        val remainderAmount: Int = remainder.amount
        val drained: Int = simulatedDrain.amount
        if (remainderAmount < drained) {
            toFill.insert(
                toDrain.drain(
                    stack.copyWithAmount(drained - remainderAmount),
                    IFluidHandler.FluidAction.EXECUTE,
                ),
                HTStorageAction.EXECUTE,
                HTStorageAccess.INTERNAl,
            )
            return true
        }
        return false
    }
}
