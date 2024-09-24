package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

object HTShaftBlock : PillarBlock(blockSettings(Blocks.CHAIN)) {
    @JvmField
    val Y_SHAPE: VoxelShape = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)

    @JvmField
    val Z_SHAPE: VoxelShape = Block.createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 16.0)

    @JvmField
    val X_SHAPE: VoxelShape = Block.createCuboidShape(0.0, 4.0, 4.0, 16.0, 12.0, 12.0)

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = when (state.get(AXIS)) {
        Direction.Axis.X -> X_SHAPE
        Direction.Axis.Y -> Y_SHAPE
        else -> Z_SHAPE
    }

    //    HTKineticNode    //

    /*override fun findProcessor(world: World, pos: BlockPos, from: Direction): BlockPos? {
        val state: BlockState = world.getBlockState(pos)
        val axis: Direction.Axis = state.get(AXIS)
        return if (axis == from.axis) {
            val toPos: BlockPos = pos.offset(from.opposite)
            val toBlock: Block = world.getBlockState(toPos).block
            return when {
                world.getBlockEntity(toPos) is HTKineticProcessor -> toPos
                toBlock is HTKineticNode -> toBlock.findProcessor(world, toPos, from)
                else -> null
            }
        } else {
            null
        }
    }*/
}
