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
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler
import net.neoforged.neoforge.network.PacketDistributor

/**
 * tick処理を行う[HTBlockEntity]
 */
abstract class HTTickAwareBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
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
            blockEntity: HTTickAwareBlockEntity,
        ) {
            val serverLevel: ServerLevel = level as? ServerLevel ?: return
            if (blockEntity.shouldTick) {
                val result: TriState = blockEntity.serverTickPre(serverLevel, pos, state)
                when (result) {
                    TriState.TRUE -> blockEntity.shouldTick = true
                    TriState.DEFAULT -> return
                    TriState.FALSE -> blockEntity.shouldTick = false
                }
                blockEntity.serverTickPost(serverLevel, pos, state, result)
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
     * サーバー側でのtick処理を行います。
     * @return 続けてtick処理を行う場合は[TriState.TRUE], 止める場合は[TriState.FALSE], 現在の状態を維持する場合は[TriState.DEFAULT]
     */
    abstract fun serverTickPre(level: ServerLevel, pos: BlockPos, state: BlockState): TriState

    /**
     * サーバー側でのtick処理を行います。
     *
     * [serverTickPre]の後で呼び出されます。
     */
    open fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        result: TriState,
    ) {}

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

    protected abstract val containerData: ContainerData

    protected fun createDefinition(): HTMenuDefinition = HTMenuDefinition(EmptyItemHandler.INSTANCE, upgrades, containerData)

    protected fun createDefinition(inventory: IItemHandler): HTMenuDefinition = HTMenuDefinition(inventory, upgrades, containerData)
}
