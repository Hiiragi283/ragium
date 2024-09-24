package hiiragi283.ragium.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FacingBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction

object HTGearBoxBlock : FacingBlock(blockSettings()) {
    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    override fun getCodec(): MapCodec<out FacingBlock> = MapCodec.unit(this)

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = defaultState.with(FACING, ctx.playerLookDirection.opposite)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState = state.with(FACING, rotation.rotate(state.get(FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState = state.with(FACING, mirror.apply(state.get(FACING)))

    //    HTKineticNode    //

    /*override fun findProcessor(world: World, pos: BlockPos, from: Direction): BlockPos? {
        val state: BlockState = world.getBlockState(pos)
        val facing: Direction = state.get(FACING)
        val toPos: BlockPos = pos.offset(facing)
        val toBlock: Block = world.getBlockState(toPos).block
        return when {
            world.getBlockEntity(toPos) is HTKineticProcessor -> toPos
            toBlock is HTKineticNode -> toBlock.findProcessor(world, toPos, facing.opposite)
            else -> null
        }
    }*/
}
