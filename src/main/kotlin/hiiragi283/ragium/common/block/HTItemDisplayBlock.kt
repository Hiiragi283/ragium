package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.common.block.entity.HTItemDisplayBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

object HTItemDisplayBlock : HTBlockWithEntity(blockSettings().nonOpaque()) {
    override fun isTransparent(state: BlockState, world: BlockView, pos: BlockPos): Boolean = true

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTItemDisplayBlockEntity(pos, state)
}
