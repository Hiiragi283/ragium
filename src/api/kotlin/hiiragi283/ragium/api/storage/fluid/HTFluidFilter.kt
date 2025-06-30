package hiiragi283.ragium.api.storage.fluid

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank

interface HTFluidFilter {
    companion object {
        @JvmField
        val ALWAYS: HTFluidFilter = Impl(canFill = true, canDrain = true)

        @JvmField
        val FILL_ONLY: HTFluidFilter = Impl(canFill = true, canDrain = false)

        @JvmField
        val DRAIN_ONLY: HTFluidFilter = Impl(canFill = false, canDrain = true)

        @JvmField
        val VIEW_ONLY: HTFluidFilter = Impl(canFill = false, canDrain = false)
    }

    fun canFill(tanks: List<IFluidTank>, stack: FluidStack): Boolean

    fun canDrain(tanks: List<IFluidTank>, stack: FluidStack): Boolean

    fun canDrain(tanks: List<IFluidTank>, maxDrain: Int): Boolean

    private class Impl(private val canFill: Boolean, private val canDrain: Boolean) : HTFluidFilter {
        override fun canFill(tanks: List<IFluidTank>, stack: FluidStack): Boolean = canFill

        override fun canDrain(tanks: List<IFluidTank>, stack: FluidStack): Boolean = canDrain

        override fun canDrain(tanks: List<IFluidTank>, maxDrain: Int): Boolean = canDrain
    }
}
