package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.entity.HTPipeBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ConnectingBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

class HTPipeBlock(private val tier: HTMachineTier, private val type: HTPipeType) : HTBlockWithEntity(blockSettings().solid().nonOpaque()) {
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
        (world.getBlockEntity(pos) as? HTPipeBlockEntity)?.canConnect(direction) ?: false,
    )

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTPipeBlockEntity(pos, state, tier, type)
}
