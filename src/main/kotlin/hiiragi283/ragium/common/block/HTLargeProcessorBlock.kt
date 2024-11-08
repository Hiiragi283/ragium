package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.machine.HTLargeProcessorBlockEntity
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTLargeProcessorBlock(settings: Settings) : HTBlockWithEntity.Horizontal(settings) {
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.INVISIBLE

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTLargeProcessorBlockEntity(pos, state)
}
