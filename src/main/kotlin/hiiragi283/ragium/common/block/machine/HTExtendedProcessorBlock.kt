package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.common.block.machine.process.HTExtendedProcessorBlockEntity
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTExtendedProcessorBlock(settings: Settings) : HTBlockWithEntity.Horizontal(settings) {
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.INVISIBLE

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTExtendedProcessorBlockEntity(pos, state)
}
