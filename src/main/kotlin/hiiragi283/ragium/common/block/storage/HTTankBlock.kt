package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.block.HTBlockWithDescription
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.ragium.common.text.RagiumTranslation
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class HTTankBlock(properties: Properties) :
    HTBasicEntityBlock(RagiumBlockEntityTypes.TANK, properties),
    HTBlockWithDescription {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE

    override fun getDescription(): HTTranslation = RagiumTranslation.TANK
}
