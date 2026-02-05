package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTMutableFluidTank
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTCreativeTankBlockEntity(pos: BlockPos, state: BlockState) : HTTankBlockEntity(RagiumBlockEntityTypes.CREATIVE_TANK, pos, state) {
    private var fluid: HTFluidResourceType? = null

    override fun createTank(listener: HTContentListener): HTMutableFluidTank = CreativeFluidTank()

    private inner class CreativeFluidTank :
        HTMutableFluidTank(),
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        override fun setResource(resource: HTFluidResourceType?) {
            fluid = resource
        }

        override fun setAmount(amount: Int) {}

        override fun getAmount(): Int = Int.MAX_VALUE

        override fun getResource(): HTFluidResourceType? = fluid

        override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

        override fun isValid(resource: HTFluidResourceType): Boolean = false
    }
}
