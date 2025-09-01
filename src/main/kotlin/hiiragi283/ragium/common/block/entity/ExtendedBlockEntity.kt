package hiiragi283.ragium.common.block.entity

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.block.entity.HTBlockEntityExtension
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.network.HTBlockEntityUpdatePacket
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.network.PacketDistributor
import org.slf4j.Logger

/**
 * Ragiumで使用する[BlockEntity]の拡張クラス
 * @see [mekanism.common.tile.base.TileEntityUpdateable]
 */
abstract class ExtendedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTBlockEntityExtension {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    override val isClientSide: Boolean get() = !(level?.isClientSide ?: true)

    //    Save & Read    //

    final override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = getReducedUpdateTag(registries)

    override fun getReducedUpdateTag(registries: HolderLookup.Provider): CompoundTag = super.getUpdateTag(registries)

    final override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        loadAdditional(tag, lookupProvider)
    }

    final override fun onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket, lookupProvider: HolderLookup.Provider) {
        val tag: CompoundTag = pkt.tag
        if (!tag.isEmpty) handleUpdateTag(tag, lookupProvider)
    }

    fun sendUpdatePacket(level: ServerLevel) {
        if (isRemoved) return
        val packet: HTBlockEntityUpdatePacket = HTBlockEntityUpdatePacket.create(this) ?: return
        PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos(blockPos), packet)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        super.setBlockState(blockState)
        afterUpdateState(blockState)
    }

    final override fun setLevel(level: Level) {
        super.setLevel(level)
        afterLevelInit(level)
    }

    //    HTContentListener    //

    protected fun setOnlySave() {
        setChanged(false)
    }

    override fun setChanged() {
        setChanged(true)
    }

    private var lastSaveTime: Long = 0

    /**
     * @see [mekanism.common.tile.base.TileEntityUpdateable.setChanged]
     */
    protected fun setChanged(updateComparator: Boolean) {
        val level: Level = this.level ?: return
        if (level.isClientSide) return
        val time: Long = level.gameTime
        if (lastSaveTime != time) {
            level.blockEntityChanged(blockPos)
            lastSaveTime = time
        }
        LOGGER.debug("Block Entity at {} will be saved", blockPos)
        if (updateComparator && !isClientSide) {
            markDirtyComparator()
        }
    }

    protected open fun markDirtyComparator() {}
}
