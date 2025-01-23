package hiiragi283.ragium.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class HTSweetBerriesCakeBlock(properties: Properties) : Block(properties) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(13.0, 0.0, 1.0, 15.0, 8.0, 15.0)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE
}
