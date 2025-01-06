package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.block.HTBlockWithEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class HTCreativeExporterBlock(settings: Settings) : HTBlockWithEntity(settings) {
    init {
        defaultState = stateManager.defaultState.with(Properties.FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? = defaultState.with(Properties.FACING, ctx.side.opposite)

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState = state.rotate(mirror.getRotation(state.get(Properties.FACING)))

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTCreativeExporterBlockEntity(pos, state)
}
