package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.alchemy.RagiElement
import net.minecraft.block.*
import net.minecraft.fluid.Fluids
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random

class HTBuddingCrystalBlock(private val element: RagiElement, settings: Settings) : AmethystBlock(settings) {
    override fun randomTick(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
        random: Random,
    ) {
        if (random.nextInt(5) == 0) {
            val direction: Direction = Direction.entries[random.nextInt(6)]
            val posTo: BlockPos = pos.offset(direction)
            val stateTo: BlockState = world.getBlockState(posTo)
            val block: Block = when {
                BuddingAmethystBlock.canGrowIn(stateTo) && element.canGrow(world, pos) -> element.clusterBlock
                else -> null
            } ?: return
            world.setBlockState(
                posTo,
                block.defaultState
                    .with(AmethystClusterBlock.FACING, direction)
                    .with(AmethystClusterBlock.WATERLOGGED, stateTo.fluidState.fluid == Fluids.WATER),
            )
        }
    }
}
