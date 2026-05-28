package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toStackOrEmpty
import hiiragi283.core.impl.storage.fluid.HTFluidStackResourceSlot
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTCreativeTankBlockEntity(pos: BlockPos, state: BlockState) : HTTankBlockEntity(RagiumBlockEntityTypes.CREATIVE_TANK, pos, state) {
    override fun createTank(listener: HTContentListener): HTFluidStackResourceSlot = CreativeFluidTank()

    override fun isCreative(): Boolean = true

    override fun getSlotSyncType(): HTSyncType = HTSyncType.BOTH

    private inner class CreativeFluidTank :
        HTFluidStackResourceSlot(),
        HTContentListener.Empty {
        private var fluid: HTFluidResourceType? = null

        override fun getStack(): FluidStack = fluid.toStackOrEmpty(Int.MAX_VALUE)

        override fun setStack(stack: FluidStack) {
            setStackInternal(stack)
        }

        override fun setStackInternal(stack: FluidStack) {
            fluid = getResourceFrom(stack)
            setChanged()
        }

        override fun updateAmount(newAmount: Int) {}

        override fun isValid(resource: HTFluidResourceType): Boolean = true

        override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

        override fun serialize(output: HTValueOutput) {
            output.write(HTConst.FLUID, HTFluidResourceType.CODEC, fluid)
        }

        override fun deserialize(input: HTValueInput) {
            fluid = input.read(HTConst.FLUID, HTFluidResourceType.CODEC)
        }
    }
}
