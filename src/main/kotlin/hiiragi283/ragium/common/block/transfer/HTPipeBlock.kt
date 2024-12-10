package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.common.block.HTBlockWithEntity
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

open class HTPipeBlock : HTBlockWithEntity(blockSettings().solid().nonOpaque().strength(2f, 6f)) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)
    }

    final override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = SHAPE

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? = null
}
