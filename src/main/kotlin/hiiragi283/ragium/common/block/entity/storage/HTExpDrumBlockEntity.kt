package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTExpDrumBlockEntity(pos: BlockPos, state: BlockState) : HTDrumBlockEntity(RagiumBlocks.EXP_DRUM, pos, state) {
    override fun createTank(listener: HTContentListener): HTFluidStackTank =
        HTFluidStackTank.create(listener, Int.MAX_VALUE, filter = RagiumFluidContents.EXPERIENCE::isOf)
}
