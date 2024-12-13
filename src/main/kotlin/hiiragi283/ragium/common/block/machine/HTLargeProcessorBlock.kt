package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.common.block.machine.process.HTLargeProcessorBlockEntity
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

object HTLargeProcessorBlock : HTBlockWithEntity.Horizontal(blockSettings(Blocks.SMOOTH_STONE)) {
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.INVISIBLE

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTLargeProcessorBlockEntity(pos, state)
}
