package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class HTLavaSpongeBlock(properties: Properties) : Block(properties) {
    override fun onPlace(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        oldState: BlockState,
        movedByPiston: Boolean,
    ) {
        if (!oldState.`is`(state.block)) {
            tryAbsorbFluid(level, pos)
        }
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean,
    ) {
        tryAbsorbFluid(level, pos)
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
    }

    private fun tryAbsorbFluid(level: Level, pos: BlockPos) {
        if (removeFluidBreadthFirstSearch(level, pos)) {
            level.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS)
        }
    }

    private fun removeFluidBreadthFirstSearch(level: Level, pos: BlockPos): Boolean = BlockPos
        .betweenClosed(-2, -2, -2, 2, 2, 2)
        .map(pos::offset)
        .filter { posIn: BlockPos ->
            val state: BlockState = level.getBlockState(pos)
            state.`is`(Blocks.LAVA)
        }.onEach { posIn: BlockPos -> level.removeBlock(posIn, false) }
        .size > 1
}
