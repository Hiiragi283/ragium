package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTBlockWithEntity
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class HTCrateBlock(val tier: HTMachineTier) : HTBlockWithEntity.Horizontal(blockSettings(Blocks.SMOOTH_STONE)) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTCrateBlockEntity(pos, state, tier)
}
