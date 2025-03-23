package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState

/**
 * tick処理を行う[HTBlockEntity]
 */
abstract class HTTickAwareBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state) {
    /**
     * tick処理を行うかどうか判定します。
     * @see [setChanged]
     */
    protected var shouldTick: Boolean = true
        private set

    protected open fun updateShouldTick(flag: Boolean) {
        shouldTick = flag
    }

    override fun setChanged() {
        super.setChanged()
        shouldTick = true
    }

    companion object {
        /**
         * クライアント側でのtick処理を行います。
         */
        @JvmStatic
        fun clientTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTTickAwareBlockEntity,
        ) {
            blockEntity.totalTick++
        }

        /**
         * サーバー側でのtick処理を行います。
         */
        @JvmStatic
        fun serverTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTTickAwareBlockEntity,
        ) {
            blockEntity.totalTick++
            if (blockEntity.shouldTick) {
                val result: TriState = blockEntity.onServerTick(level, pos, state)
                when (result) {
                    TriState.TRUE -> blockEntity.updateShouldTick(true)
                    TriState.DEFAULT -> return
                    TriState.FALSE -> blockEntity.updateShouldTick(false)
                }
            }
        }
    }

    /**
     * サーバー側でのtick処理を行います。
     * @return 続けてtick処理を行う場合は[TriState.TRUE], 止める場合は[TriState.FALSE], 現在の状態を維持する場合は[TriState.DEFAULT]
     */
    abstract fun onServerTick(level: Level, pos: BlockPos, state: BlockState): TriState
}
