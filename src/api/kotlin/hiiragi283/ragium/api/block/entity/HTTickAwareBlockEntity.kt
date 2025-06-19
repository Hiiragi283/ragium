package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState

/**
 * tick処理を行う[HTBlockEntity]
 */
abstract class HTTickAwareBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state) {
    /**
     * このブロックエンティティが生成されてからの経過時間
     *
     * セーブのたびにリセットされる
     */
    var totalTick: Int = 0
        protected set

    /**
     * このブロックエンティティが稼働する時間間隔
     */
    abstract val maxTicks: Int

    /**
     * tick処理を行うかどうか判定します。
     * @see [setChanged]
     */
    protected var shouldTick: Boolean = true
        private set

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
            if (blockEntity.shouldTick && level is ServerLevel) {
                val result: TriState = blockEntity.onServerTick(level, pos, state)
                val oldFlag: Boolean = blockEntity.shouldTick
                when (result) {
                    TriState.TRUE -> blockEntity.shouldTick = true
                    TriState.DEFAULT -> return
                    TriState.FALSE -> blockEntity.shouldTick = false
                }
                if (oldFlag != blockEntity.shouldTick) {
                    blockEntity.sendUpdatePacket(level)
                }
            }
        }
    }

    /**
     * サーバー側でのtick処理を行います。
     * @return 続けてtick処理を行う場合は[TriState.TRUE], 止める場合は[TriState.FALSE], 現在の状態を維持する場合は[TriState.DEFAULT]
     */
    abstract fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState

    protected val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> totalTick
            1 -> maxTicks
            else -> -1
        }

        override fun set(index: Int, value: Int) {}

        override fun getCount(): Int = 2
    }

    protected fun canProcess(): Boolean = totalTick > 0 && totalTick % maxTicks == 0
}
