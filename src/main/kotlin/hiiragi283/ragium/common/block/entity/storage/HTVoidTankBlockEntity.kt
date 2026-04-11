package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.impl.storage.fluid.HTFluidStackResourceSlot
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTVoidTankBlockEntity(pos: BlockPos, state: BlockState) : HTTankBlockEntity(RagiumBlockEntityTypes.VOID_TANK, pos, state) {
    override fun createTank(listener: HTContentListener): HTFluidStackResourceSlot =
        object : HTFluidStackResourceSlot(), HTContentListener.Empty, HTValueSerializable.Empty {
            override fun getStack(): FluidStack = FluidStack.EMPTY

            override fun setStack(stack: FluidStack) {}

            override fun setStackInternal(stack: FluidStack) {}

            override fun updateAmount(newAmount: Int) {}

            override fun isValid(resource: HTFluidResourceType): Boolean = true

            override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE
        }
}
