package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTMutableFluidTank
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTVoidTankBlockEntity(pos: BlockPos, state: BlockState) : HTTankBlockEntity(RagiumBlockEntityTypes.VOID_TANK, pos, state) {
    override fun createTank(listener: HTContentListener): HTMutableFluidTank =
        object : HTMutableFluidTank(), HTContentListener.Empty, HTValueSerializable.Empty {
            override fun setResource(resource: HTFluidResourceType?) {}

            override fun setAmount(amount: Int) {}

            override fun getAmount(): Int = 0

            override fun getResource(): HTFluidResourceType? = null

            override fun getCapacity(resource: HTFluidResourceType?): Int = Int.MAX_VALUE

            override fun isValid(resource: HTFluidResourceType): Boolean = true
        }
}
