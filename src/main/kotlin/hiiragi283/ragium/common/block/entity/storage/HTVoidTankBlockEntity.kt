package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.impl.storage.fluid.HTFluidStackResourceSlot
import hiiragi283.ragium.common.storge.fluid.HTVoidItemFluidTank
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTVoidTankBlockEntity(pos: BlockPos, state: BlockState) : HTTankBlockEntity(RagiumBlockEntityTypes.VOID_TANK, pos, state) {
    override fun createTank(listener: HTContentListener): HTFluidStackResourceSlot = HTVoidItemFluidTank()

    override fun getSlotSyncType(): HTSyncType? = null
}
