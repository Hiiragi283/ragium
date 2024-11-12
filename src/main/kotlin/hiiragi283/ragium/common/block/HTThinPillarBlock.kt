package hiiragi283.ragium.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.PillarBlock
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class HTThinPillarBlock(settings: Settings) : PillarBlock(settings) {
    companion object {
        @JvmField
        val Y_SHAPE: VoxelShape = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)

        @JvmField
        val Z_SHAPE: VoxelShape = Block.createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 16.0)

        @JvmField
        val X_SHAPE: VoxelShape = Block.createCuboidShape(0.0, 4.0, 4.0, 16.0, 12.0, 12.0)
    }

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
}
