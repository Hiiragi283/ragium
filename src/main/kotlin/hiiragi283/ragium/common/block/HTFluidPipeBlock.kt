package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTFluidPipeBlockEntity
import hiiragi283.ragium.common.init.RagiumBlocks
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ConnectingBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class HTFluidPipeBlock(settings: Settings) : HTBlockWithEntity(settings) {
    companion object {
        @JvmStatic
        fun canConnect(world: World, pos: BlockPos, dir: Direction): Boolean {
            val posTo: BlockPos = pos.offset(dir)
            val stateTo: BlockState = world.getBlockState(posTo)
            return when {
                stateTo.isOf(RagiumBlocks.FLUID_PIPE) -> true
                else -> ItemStorage.SIDED.find(
                    world,
                    posTo,
                    dir.opposite,
                ) != null
            }
        }
    }

    init {
        defaultState = stateManager.defaultState
            .with(Properties.DOWN, false)
            .with(Properties.UP, false)
            .with(Properties.NORTH, false)
            .with(Properties.SOUTH, false)
            .with(Properties.WEST, false)
            .with(Properties.EAST, false)
            .with(Properties.FACING, Direction.DOWN)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(
            Properties.DOWN,
            Properties.UP,
            Properties.NORTH,
            Properties.SOUTH,
            Properties.WEST,
            Properties.EAST,
            Properties.FACING,
        )
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = defaultState.with(Properties.FACING, ctx.side.opposite)

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState = state.with(
        ConnectingBlock.FACING_PROPERTIES[direction],
        (world.getBlockEntity(pos) as? HTFluidPipeBlockEntity)?.canConnect(direction) ?: false,
    )

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTFluidPipeBlockEntity(pos, state)
}
