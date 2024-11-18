package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.entity.HTExporterBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
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

class HTExporterBlock(private val tier: HTMachineTier) : HTBlockWithEntity(blockSettings().solid().nonOpaque().strength(2f, 6f)) {
    init {
        defaultState = stateManager.defaultState.with(Properties.FACING, Direction.NORTH)
    }

    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType,
    ) {
        HTPipeType.ALL.appendTooltip(tooltip::add, tier)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? = defaultState.with(Properties.FACING, ctx.side.opposite)

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState = state.rotate(mirror.getRotation(state.get(Properties.FACING)))

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTExporterBlockEntity(pos, state, tier)
}
