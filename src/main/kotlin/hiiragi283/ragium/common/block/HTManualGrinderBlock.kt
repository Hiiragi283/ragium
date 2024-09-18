package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTManualGrinderBlockEntity
import hiiragi283.ragium.common.init.RagiumBlocks.Properties.LEVEL_7
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTManualGrinderBlock : HTBlockWithEntity(blockSettings(Blocks.SMOOTH_STONE)) {
    init {
        defaultState = stateManager.defaultState.with(LEVEL_7, 0)
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = state.get(LEVEL_7)

    override fun hasComparatorOutput(state: BlockState): Boolean = true

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(LEVEL_7)
    }

    override fun hasSidedTransparency(state: BlockState): Boolean = true

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTManualGrinderBlockEntity(pos, state)
}
