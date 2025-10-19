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
 * @see [me.desht.pneumaticcraft.common.block.entity.AbstractFluidTankBlockEntity]
 */
interface HTFluidItemSlot :
    HTItemSlot,
    HTStackView.Mutable<ImmutableItemStack> {
    fun getFluidTank(): HTFluidTank

    /**
     * @see [mekanism.common.inventory.slot.IFluidHandlerSlot.fillTank]
     */
    fun fillTank(moveTo: HTItemSlot.Mutable): Boolean {
        val stacks: List<ImmutableFluidStack> = RagiumCapabilities.FLUID.getCapabilityStacks(getStack())
        if (stacks.size == 1) {
            val stackIn: ImmutableFluidStack = stacks[0]
            if (!stackIn.isEmpty() && getFluidTank().isValid(stackIn)) {
                return drainItemAndMove(moveTo, stackIn)
            }
        } else if (stacks.size > 1) {
            var changed = false
            for (tank: ImmutableFluidStack in stacks) {
                if (drainItemAndMove(moveTo, tank) && this.isEmpty()) {
                    changed = true
                    break
                }
            }
            return changed
        }
        return false
    }

    /**
     * @see [mekanism.common.inventory.slot.IFluidHandlerSlot.drainTank]
     */
    fun drainTank(moveTo: HTItemSlot.Mutable) {
        if (!RagiumCapabilities.FLUID.hasCapability(this.getStack())) return
        val stackIn: ImmutableFluidStack = this.getFluidTank().getStack()
        if (!stackIn.isEmpty()) {
            val simulatedDrain: ImmutableFluidStack = this.getFluidTank().extract(
                stackIn.amountAsInt(),
                HTStorageAction.SIMULATE,
                HTStorageAccess.INTERNAL,
            )
            if (simulatedDrain.isEmpty()) return

            val input: ImmutableItemStack = this.getStack().copyWithAmount(1)
            val handlerItem: IFluidHandlerItem = RagiumCapabilities.FLUID.getCapability(input) ?: return
            val toDrain: Int = handlerItem.fill(stackIn.copy().stack, HTStorageAction.SIMULATE.toFluid())
            if (toDrain == 0) return
            if (this.getAmountAsInt() == 1) {
                RagiumCapabilities.FLUID.getCapability(handlerItem.container)?.let { fluidHandlerItem: IFluidHandlerItem ->
                    if (fluidHandlerItem.fill(stackIn.copy().stack, HTStorageAction.SIMULATE.toFluid()) > 0) {
                        setItemStack(handlerItem.container)
                        this.getFluidTank().extract(toDrain, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                        return
                    }
                }
            }
            if (moveItem(moveTo, handlerItem.container.toImmutable())) {
                this.getFluidTank().extract(toDrain, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            }
        }
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
            RagiumCapabilities.FLUID.getCapability(handlerItem.container)?.let { fluidHandlerItem: IFluidHandlerItem ->
                if (!fluidHandlerItem.drain(Int.MAX_VALUE, HTStorageAction.SIMULATE.toFluid()).isEmpty) {
                    setItemStack(handlerItem.container)
                    this.getFluidTank().insertFluid(drained, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
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
        val handlerItem: IFluidHandlerItem = RagiumCapabilitiesNew.INSTANCE.fluid.getCapability(getStack()) ?: return false
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
            for (i: Int in (0.<tanks)) {
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
