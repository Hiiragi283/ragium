package hiiragi283.ragium.common.storge.fluid

import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTItemFluidTank
import hiiragi283.core.support.storage.fluid.HTFluidStackResourceSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTVoidItemFluidTank(override val container: ItemStack) :
    HTFluidStackResourceSlot(),
    HTItemFluidTank {
    constructor() : this(ItemStack.EMPTY)

    override fun getStack(): FluidStack = FluidStack.EMPTY

    override fun setStack(stack: FluidStack) {}

    override fun setStackInternal(stack: FluidStack) {}

    override fun updateAmount(newAmount: Int) {}

    override fun isValid(resource: HTFluidResourceType): Boolean = true

    override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE
}
