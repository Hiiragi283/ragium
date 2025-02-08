package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

internal class HTMachineFluidTankImpl(private val baseCapacity: Int, private val callback: () -> Unit) :
    FluidTank(baseCapacity),
    HTMachineFluidTank {
    override fun onContentsChanged() {
        callback()
    }

    override fun setFluid(stack: FluidStack) {
        super.setFluid(stack)
    }

    override fun updateCapacity(blockEntity: HTEnchantableBlockEntity) {
        val level: Int = blockEntity.getEnchantmentLevel(RagiumEnchantments.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    //    HTSlotHandler    //

    override var stack: FluidStack
        get() = fluid
        set(value) = setFluid(value)
}
