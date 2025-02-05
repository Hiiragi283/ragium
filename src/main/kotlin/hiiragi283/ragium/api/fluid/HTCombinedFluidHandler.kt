package hiiragi283.ragium.api.fluid

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.EmptyFluidHandler

class HTCombinedFluidHandler(vararg fluidHandlers: IFluidHandler) : IFluidHandler {
    private val handlers: Array<out IFluidHandler> = fluidHandlers
    private val baseIndex: Array<Int> = Array(handlers.size) { 0 }
    private val slotCount: Int

    init {
        var index = 0
        handlers.forEachIndexed { i: Int, handler: IFluidHandler ->
            index += handlers.size
            baseIndex[i] = index
        }
        this.slotCount = index
    }

    private fun getIndexForSlot(slot: Int): Int {
        if (slot < 0) {
            return -1
        }
        baseIndex.forEachIndexed { i: Int, index: Int ->
            if (slot - index < 0) {
                return i
            }
        }
        return -1
    }

    private fun getHandlerFromIndex(index: Int): IFluidHandler {
        if (index < 0 || index >= handlers.size) {
            return EmptyFluidHandler.INSTANCE
        }
        return handlers[index]
    }

    private fun getSlotFromIndex(slot: Int, index: Int): Int {
        if (index <= 0 || index >= baseIndex.size) {
            return slot
        }
        return slot - baseIndex[index]
    }

    override fun getTanks(): Int = slotCount

    override fun getFluidInTank(tank: Int): FluidStack {
        val index: Int = getIndexForSlot(tank)
        val handler: IFluidHandler = getHandlerFromIndex(index)
        val slot1: Int = getSlotFromIndex(tank, index)
        return handler.getFluidInTank(slot1)
    }

    override fun getTankCapacity(tank: Int): Int {
        val index: Int = getIndexForSlot(tank)
        val handler: IFluidHandler = getHandlerFromIndex(index)
        val slot1: Int = getSlotFromIndex(tank, index)
        return handler.getTankCapacity(slot1)
    }

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean {
        val index: Int = getIndexForSlot(tank)
        val handler: IFluidHandler = getHandlerFromIndex(index)
        val slot1: Int = getSlotFromIndex(tank, index)
        return handler.isFluidValid(slot1, stack)
    }

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        TODO("Not yet implemented")
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        TODO("Not yet implemented")
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        TODO("Not yet implemented")
    }
}
