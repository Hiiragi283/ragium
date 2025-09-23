package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.value.HTValueInput
import net.neoforged.neoforge.fluids.FluidStack

object HTEmptyFluidTank : HTFluidTank {
    override fun getStack(): FluidStack = FluidStack.EMPTY

    override fun setStack(stack: FluidStack) {}

    override fun getCapacity(): Int = 0

    override fun isFluidValid(stack: FluidStack): Boolean = false

    override fun deserialize(input: HTValueInput) {}

    override fun onContentsChanged() {}
}
