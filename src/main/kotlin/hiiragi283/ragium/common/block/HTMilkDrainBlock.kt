package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.common.block.entity.HTMilkDrainBlockEntity
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class HTMilkDrainBlock(properties: Properties) : HTEntityBlock<HTMilkDrainBlockEntity>(RagiumBlockEntityTypes.MILK_DRAIN, properties) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE

    override fun initDefaultState(): BlockState = stateDefinition.any()
}
