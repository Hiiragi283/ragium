package hiiragi283.ragium.support.storage.fluid

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTItemFluidTank
import hiiragi283.core.support.storage.fluid.HTFluidStackResourceSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTVoidItemFluidTank(override val container: ItemStack) :
    HTFluidStackResourceSlot(),
    HTItemFluidTank,
    HTContentListener by HTContentListener.NOTHING,
    HTValueSerializable by HTValueSerializable.NOTHING {
    constructor() : this(ItemStack.EMPTY)

    override fun getStack(): FluidStack = FluidStack.EMPTY

    override fun setStack(stack: FluidStack) {}

    override fun setStackInternal(stack: FluidStack) {}

    override fun updateAmount(newAmount: Int) {}

    override fun isValid(resource: HTFluidResourceType): Boolean = true

    override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE
}
