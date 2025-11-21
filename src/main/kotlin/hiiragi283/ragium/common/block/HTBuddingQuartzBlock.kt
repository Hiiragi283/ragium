package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.collection.random
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.AmethystBlock
import net.minecraft.world.level.block.AmethystClusterBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids

/**
 * @see net.minecraft.world.level.block.BuddingAmethystBlock
 * @see appeng.decorative.solid.BuddingCertusQuartzBlock
 */
class HTBuddingQuartzBlock(properties: Properties) : AmethystBlock(properties) {
    companion object {
        @JvmStatic
        fun canClusterGrowAt(state: BlockState): Boolean = state.isAir || state.`is`(Blocks.WATER) || state.fluidState.amount == 8
    }

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        if (random.nextInt(5) != 0) return

        val direction: Direction = Direction.entries.random(random)
        val posTo: BlockPos = pos.relative(direction)
        val stateTo: BlockState = level.getBlockState(posTo)
        if (canClusterGrowAt(stateTo)) {
            val cluster: BlockState = RagiumBlocks.QUARTZ_CLUSTER
                .get()
                .defaultBlockState()
                .setValue(AmethystClusterBlock.FACING, direction)
                .setValue(AmethystClusterBlock.WATERLOGGED, stateTo.fluidState.type == Fluids.WATER)
            level.setBlockAndUpdate(posTo, cluster)
        }
    }
}
