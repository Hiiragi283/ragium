package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.extension.machineTypeOrNull
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
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
import net.minecraft.util.ItemScatterer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.WorldView

object HTMetaMachineBlock : HTBlockWithEntity(blockSettings(Blocks.IRON_BLOCK)) {
    init {
        defaultState = stateManager.defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType,
    ) {
        stack.machineTypeOrNull?.appendTooltip(tooltip::add, stack.machineTier)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing.opposite)

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        if (!state.isOf(newState.block)) {
            world.getMachineEntity(pos)?.let { machine: HTMachineEntity ->
                ItemScatterer.spawn(world, pos, machine)
                world.updateComparators(pos, state.block)
            }
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING)
    }

    override fun getPickStack(world: WorldView, pos: BlockPos, state: BlockState): ItemStack =
        world.getMachineEntity(pos)?.let { it.machineType.createItemStack(it.tier) }
            ?: super.getPickStack(world, pos, state)

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)))

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTMetaMachineBlockEntity(pos, state)
}
