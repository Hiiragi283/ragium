package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * [EntityBlock]の拡張インターフェース
 * @see mekanism.common.block.interfaces.IHasTileEntity
 */
interface HTBlockWithEntity : EntityBlock {
    fun getBlockEntityType(): HTDeferredBlockEntityType<*>

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = getBlockEntityType().create(pos, state)

    @Suppress("UNCHECKED_CAST")
    override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
        val type: HTDeferredBlockEntityType<*> = getBlockEntityType()
        return when (blockEntityType) {
            type.get() -> type.getTicker(level.isClientSide) as? BlockEntityTicker<T>
            else -> null
        }
    }
}
