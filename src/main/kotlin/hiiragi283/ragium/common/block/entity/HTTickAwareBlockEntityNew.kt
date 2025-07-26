package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.network.HTBlockEntityUpdatePacket
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.network.PacketDistributor

/**
 * tick処理を行う[HTBlockEntity]
 */
abstract class HTTickAwareBlockEntityNew(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state) {
    companion object {
        /**
         * サーバー側でのtick処理を行います。
         */
        @JvmStatic
        fun serverTick(
            level: Level,
            pos: BlockPos,
            state: BlockState,
            blockEntity: HTTickAwareBlockEntityNew,
        ) {
            val serverLevel: ServerLevel = level as? ServerLevel ?: return
            if (blockEntity.shouldTick) {
                val result: TriState = blockEntity.onServerTick(serverLevel, pos, state)
                when (result) {
                    TriState.TRUE -> blockEntity.shouldTick = true
                    TriState.DEFAULT -> return
                    TriState.FALSE -> blockEntity.shouldTick = false
                }
            }
            if (blockEntity.shouldSync) {
                blockEntity.sendUpdatePacket(level)
                blockEntity.shouldSync = false
            }
        }
    }

    //    Ticking    //

    /**
     * tick処理を行うかどうか判定します。
     * @see [setChanged]
     */
    protected var shouldTick: Boolean = true
        private set

    /**
     * このブロックエンティティが稼働する時間間隔
     */
    abstract var maxTicks: Int
        protected set

    var currentTicks: Int = 0
        protected set

    /**
     * サーバー側でのtick処理を行います。
     * @return 続けてtick処理を行う場合は[TriState.TRUE], 止める場合は[TriState.FALSE], 現在の状態を維持する場合は[TriState.DEFAULT]
     */
    abstract fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState

    //    Sync    //

    override fun setChanged() {
        super.setChanged()
        shouldTick = true
        shouldSync = true
    }

    /**
     * 同期処理を行うかどうか判定します。
     * @see [setChanged]
     */
    protected var shouldSync: Boolean = false

    /**
     * クライアント側に同期パケットを送る
     */
    fun sendUpdatePacket(serverLevel: ServerLevel) {
        sendUpdatePacket(serverLevel) { payload: CustomPacketPayload ->
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos(blockPos), payload)
        }
    }

    protected open fun sendUpdatePacket(serverLevel: ServerLevel, consumer: (CustomPacketPayload) -> Unit) {
        consumer(HTBlockEntityUpdatePacket(blockPos, getUpdateTag(serverLevel.registryAccess())))
    }

    //    Menu    //

    protected val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> currentTicks
            1 -> maxTicks
            else -> -1
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> currentTicks = value
                1 -> maxTicks = value
                else -> {}
            }
        }

        override fun getCount(): Int = 2
    }

    protected fun createDefinition(inventory: IItemHandler): HTMenuDefinition = HTMenuDefinition(inventory, upgrades, containerData)
}
