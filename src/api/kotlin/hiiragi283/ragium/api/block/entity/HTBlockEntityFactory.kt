package hiiragi283.ragium.api.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * [BlockEntityType.BlockEntitySupplier]の拡張インターフェース
 */
fun interface HTBlockEntityFactory<BE : BlockEntity> :
    BlockEntityType.BlockEntitySupplier<BE>,
    (BlockPos, BlockState) -> BE {
    override fun create(pos: BlockPos, state: BlockState): BE = invoke(pos, state)
}
