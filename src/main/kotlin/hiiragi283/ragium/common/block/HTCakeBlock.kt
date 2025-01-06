package hiiragi283.ragium.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class HTCakeBlock(settings: Settings) : Block(settings) {
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = createCuboidShape(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)
}
