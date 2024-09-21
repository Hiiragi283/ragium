package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

object HTAlchemicalInfuserBlock :
    HTBlockWithEntity(RagiumBlockEntityTypes.ALCHEMICAL_INFUSER, blockSettings(Blocks.CRYING_OBSIDIAN)) {
    @JvmField
    val SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = SHAPE
}
