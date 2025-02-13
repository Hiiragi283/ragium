package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.common.block.HTEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class HTDisenchantingTableBlock(properties: Properties) : HTEntityBlock(properties) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE

    override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType): Boolean = false

    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTDisenchantingTableBlockEntity =
        HTDisenchantingTableBlockEntity(pos, state)
}
