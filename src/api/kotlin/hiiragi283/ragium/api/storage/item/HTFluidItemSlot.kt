package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.insertFluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * @see [mekanism.common.inventory.slot.IFluidHandlerSlot]
 */
interface HTFluidItemSlot :
    HTItemSlot,
    HTStackView.Mutable<ImmutableItemStack> {
    fun getFluidTank(): HTFluidTank

    var isDraining: Boolean
    var isFilling: Boolean

    /**
     * @see [mekanism.common.inventory.slot.IFluidHandlerSlot.fillTank]
     */
    fun fillTank(moveTo: HTItemSlot.Mutable): Boolean {
        val tanks: List<HTFluidTank> = RagiumCapabilities.FLUID.getCapabilitySlots(getStack())
        if (tanks.size == 1) {
            val stackIn: ImmutableFluidStack = tanks[0].getStack()
            if (!stackIn.isEmpty() && getFluidTank().isValid(stackIn)) {
                return drainItemAndMove(moveTo, stackIn)
            }
        } else if (tanks.size > 1) {
            var changed = false
            for (tank: HTFluidTank in tanks) {
                if (drainItemAndMove(moveTo, tank.getStack()) && this.isEmpty()) {
                    changed = true
                    break
                }
            }
            return changed
        }
        return false
    }

    /**
     * @see [mekanism.common.inventory.slot.IFluidHandlerSlot.drainItemAndMove]
     */
    private fun drainItemAndMove(moveTo: HTItemSlot.Mutable, fluid: ImmutableFluidStack): Boolean {
        val simulatedRemainder: ImmutableFluidStack = getFluidTank().insert(fluid, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        val remainder: Int = simulatedRemainder.amountAsInt()
        val toTransfer: Int = fluid.amountAsInt()
        if (remainder == toTransfer) {
            return false
        }

        val stack: ImmutableItemStack = this.getStack()
        if (!RagiumCapabilities.FLUID.hasCapability(stack)) return false
        val input: ImmutableItemStack = stack.copyWithAmount(1)
        val handlerItem: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(input) ?: return false

        val drained: FluidStack = handlerItem.drain(fluid.copyWithAmount(toTransfer - remainder).stack, HTStorageAction.EXECUTE.toFluid())
        if (drained.isEmpty) return false

        if (this.getAmountAsInt() == 1) {
            RagiumCapabilities.FLUID.getCapability(handlerItem.container)?.let { fluidHandlerItem ->
                if (!fluidHandlerItem.drain(Int.MAX_VALUE, HTStorageAction.SIMULATE.toFluid()).isEmpty) {
                    setItemStack(handlerItem.container)
                    this.getFluidTank().insertFluid(drained, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                    this.isFilling = true
                    return true
                }
            }
        }
        if (moveItem(moveTo, handlerItem.container.toImmutable())) {
            this.getFluidTank().insertFluid(drained, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            return true
        }
        return false
    }

    /**
     * @see [mekanism.common.inventory.slot.IFluidHandlerSlot.moveItem]
     */
    private fun moveItem(moveTo: HTItemSlot.Mutable, stackToMove: ImmutableItemStack): Boolean {
        if (moveTo.isEmpty()) {
            moveTo.setStack(stackToMove)
        } else {
            if (moveTo.insert(stackToMove, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == stackToMove) {
                return false
            }
            moveTo.growStack(1, HTStorageAction.EXECUTE)
        }
        this.shrinkStack(1, HTStorageAction.EXECUTE)
        return true
    }

    /*fun fillTank(): Boolean {
        if (this.getAmountAsInt() != 1) return false
        val handlerItem: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(getStack()) ?: return false
        val tanks: Int = handlerItem.tanks
        if (tanks == 1) {
            val stackIn: FluidStack = handlerItem.getFluidInTank(0)
            if (!stackIn.isEmpty && getFluidTank().isValid(stackIn)) {
                if (fillHandlerFromOther(getFluidTank(), handlerItem, stackIn)) {
                    setItemStack(handlerItem.container)
                    return true
                }
            }
        } else if (tanks > 1) {
            var changed = false
            for (i: Int in (0..<tanks)) {
                val stackIn: FluidStack = handlerItem.getFluidInTank(i)
                if (stackIn.isEmpty) continue
                if (fillHandlerFromOther(getFluidTank(), handlerItem, stackIn)) {
                    changed = true
                }
            }
            if (changed) {
                setItemStack(handlerItem.container)
                return true
            }
        }
        return false
    }

    private fun fillHandlerFromOther(toFill: HTFluidTank, toDrain: IFluidHandler, stack: FluidStack): Boolean {
        val simulatedDrain: FluidStack = toDrain.drain(stack.amount, HTStorageAction.SIMULATE.toFluid())
        if (simulatedDrain.isEmpty) return false
        val remainder: FluidStack = getFluidTank().insertFluid(simulatedDrain, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        val remainderAmount: Int = remainder.amount
        val drained: Int = simulatedDrain.amount
        if (remainderAmount < drained) {
            toFill.insert(
                toDrain
                    .drain(
                        drained - remainderAmount,
                        HTStorageAction.EXECUTE.toFluid(),
                    ).toImmutable(),
                HTStorageAction.EXECUTE,
                HTStorageAccess.INTERNAL,
            )
            return true
        }
        return false
    }*/
}
