package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTPipeType
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ConnectingBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

class HTSimplePipeBlock(private val tier: HTMachineTier, private val type: HTPipeType, settings: Settings) : HTPipeBlock(settings) {
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

    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType,
    ) {
        type.appendTooltip(tooltip::add, tier)
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

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = defaultState
        .with(Properties.FACING, ctx.side.opposite)
        .with(Properties.DOWN, HTPipeType.canConnect(ctx.world, ctx.blockPos, Direction.DOWN, type))
        .with(Properties.UP, HTPipeType.canConnect(ctx.world, ctx.blockPos, Direction.UP, type))
        .with(Properties.NORTH, HTPipeType.canConnect(ctx.world, ctx.blockPos, Direction.NORTH, type))
        .with(Properties.SOUTH, HTPipeType.canConnect(ctx.world, ctx.blockPos, Direction.SOUTH, type))
        .with(Properties.WEST, HTPipeType.canConnect(ctx.world, ctx.blockPos, Direction.WEST, type))
        .with(Properties.EAST, HTPipeType.canConnect(ctx.world, ctx.blockPos, Direction.EAST, type))

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState = state.rotate(mirror.getRotation(state.get(Properties.FACING)))

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState = state.with(
        ConnectingBlock.FACING_PROPERTIES[direction],
        (world.getBlockEntity(pos) as? HTSimplePipeBlockEntity)?.canConnect(direction) == true,
    )

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTSimplePipeBlockEntity(pos, state, tier, type)
}
