package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

object HTItemDisplayBlock : HTBlockWithEntity(RagiumBlockEntityTypes.ITEM_DISPLAY, blockSettings().nonOpaque()) {
    override fun isTransparent(state: BlockState, world: BlockView, pos: BlockPos): Boolean = true
}
