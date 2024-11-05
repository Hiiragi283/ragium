package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.entity.HTPipeBlockEntity
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
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
import net.minecraft.util.ItemScatterer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
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

    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType,
    ) {
        if (type.isItem) {
            tooltip.add(Text.literal("- Item Speed; ${HTPipeType.ALL.getItemCount(tier)} / s"))
        }
        if (type.isFluid) {
            tooltip.add(Text.literal("- Fluid Speed; ${HTPipeType.ALL.getFluidCount(tier)} Units / s"))
        }
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

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        ItemStorage.SIDED.find(world, pos, null)?.forEach { view: StorageView<ItemVariant> ->
            ItemScatterer.spawn(
                world,
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                view.resource.toStack(view.amount.toInt()),
            )
        }
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTPipeBlockEntity(pos, state, tier, type)
}
