package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTPlaySoundBlockEntity
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * @see [mekanism.common.tile.base.TileEntityMekanism]
 */
abstract class HTBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    ExtendedBlockEntity(
        type,
        pos,
        state,
    ) {
    companion object {
        /**
         * @see [mekanism.common.tile.base.TileEntityMekanism.tickClient]
         */
        @JvmStatic
        fun tickClient(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            if (blockEntity is HTPlaySoundBlockEntity) {
                blockEntity.playSound(level, pos)
            }
            blockEntity.onUpdateClient(level, pos, state)
            blockEntity.ticks++
        }

        /**
         * @see [mekanism.common.tile.base.TileEntityMekanism.tickServer]
         */
        @JvmStatic
        fun tickServer(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTBlockEntity,
        ) {
            val serverLevel: ServerLevel = level as? ServerLevel ?: return
            val shouldUpdate: Boolean = blockEntity.onUpdateServer(serverLevel, pos, state)
            blockEntity.ticks++
            if (shouldUpdate) {
                blockEntity.sendUpdatePacket(serverLevel)
            }
        }
    }

    var ticks: Int = 0
        protected set

    protected fun onUpdateClient(level: Level, pos: BlockPos, state: BlockState) {}

    protected abstract fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean
}
