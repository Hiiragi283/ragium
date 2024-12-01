package hiiragi283.ragium.common.block

import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.TransparentBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldView

open class HTSurfaceBlock(settings: Settings) : TransparentBlock(settings.nonOpaque()) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = SHAPE

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = VoxelShapes.empty()

    override fun hasSidedTransparency(state: BlockState): Boolean = true

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val posDown: BlockPos = pos.down()
        return isFaceFullSquare(world.getBlockState(posDown).getCollisionShape(world, posDown), Direction.UP)
    }
}
