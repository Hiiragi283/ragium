package hiiragi283.ragium.common.block

import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class HTWarpedWartBlock(properties: Properties) : HTCropBlock(RagiumItems.WARPED_WART, properties) {
    override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = state.`is`(Blocks.SOUL_SAND)
}
