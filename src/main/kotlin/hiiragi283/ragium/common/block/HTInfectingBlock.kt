package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.util.blockSettings
import hiiragi283.ragium.common.util.getOrNull
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random

object HTInfectingBlock : Block(blockSettings().ticksRandomly().dropsNothing()) {
    init {
        defaultState = stateManager.defaultState.with(Properties.ENABLED, false)
    }

    override fun randomTick(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
        random: Random,
    ) {
        Direction.entries.forEach { direction: Direction ->
            val posTo: BlockPos = pos.offset(direction)
            val stateTo: BlockState = world.getBlockState(posTo)
            if (stateTo.isOf(Blocks.WATER)) return@forEach
            val list = listOf(
                Blocks.SANDSTONE,
            )
            if (stateTo.isAir && posTo.y < 63) {
                world.setBlockState(posTo, defaultState.with(Properties.ENABLED, true))
                return@forEach
            }
        }
        if (state.getOrNull(Properties.ENABLED) == true) {
            world.setBlockState(pos, Blocks.WATER.defaultState)
            return
        }
        /*if (Direction.entries.all { world.getBlockState(pos.offset(it)).isAir }) {
            world.removeBlock(pos, false)
            return
        }*/
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.ENABLED)
    }
}