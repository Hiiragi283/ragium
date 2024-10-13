package hiiragi283.ragium.common.block

import com.google.common.collect.Table
import hiiragi283.ragium.api.extension.buildTable
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.tag.FluidTags
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class HTQuarterBlock(settings: Settings) :
    Block(settings),
    Waterloggable {
    companion object {
        @JvmField
        val SIDE: DirectionProperty = Properties.HORIZONTAL_FACING

        @JvmField
        val IS_UPPER: BooleanProperty = Properties.BOTTOM

        @JvmField
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED

        @JvmField
        val SHAPE_TABLE: Table<Direction, Boolean, VoxelShape> = buildTable {
            put(Direction.NORTH, false, createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 8.0))
            put(Direction.NORTH, true, createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 8.0))
        }
    }

    init {
        defaultState = stateManager.defaultState
            .with(SIDE, Direction.NORTH)
            .with(IS_UPPER, false)
            .with(WATERLOGGED, false)
    }

    override fun hasSidedTransparency(state: BlockState): Boolean = true

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(SIDE, IS_UPPER, WATERLOGGED)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = SHAPE_TABLE.get(state.get(SIDE), state.get(IS_UPPER))
        ?: super.getOutlineShape(state, world, pos, context)

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val world: World = ctx.world
        val pos: BlockPos = ctx.blockPos
        val isWater: Boolean = world.getFluidState(pos).isOf(Fluids.WATER)
        val side: Direction = ctx.side
        return defaultState
            .with(SIDE, ctx.horizontalPlayerFacing)
            .with(WATERLOGGED, isWater)
            .with(
                IS_UPPER,
                side != Direction.DOWN && (side == Direction.UP || !((ctx.hitPos.y - pos.y) > 0.5)),
            )
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun canPathfindThrough(state: BlockState, type: NavigationType): Boolean = when (type) {
        NavigationType.WATER -> state.fluidState.isIn(FluidTags.WATER)
        else -> false
    }
}
