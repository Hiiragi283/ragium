package hiiragi283.ragium.common.block.entity

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.block.entity.HTBlockEntityExtension
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.item.HTSlotProvider
import hiiragi283.ragium.common.network.HTBlockEntityUpdatePacket
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.Connection
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Nameable
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.network.PacketDistributor
import org.slf4j.Logger

/**
 * Ragiumで使用する[BlockEntity]の拡張クラス
 * @see [mekanism.common.tile.base.TileEntityUpdateable]
 */
abstract class ExtendedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTBlockEntityExtension,
    HTNbtCodec,
    HTSlotProvider,
    Nameable {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    override val isRemote: Boolean get() = !(level?.isClientSide ?: true)

    //    Save & Load    //

    final override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = getReducedUpdateTag(registries)

    override fun getReducedUpdateTag(registries: HolderLookup.Provider): CompoundTag = saveCustomOnly(registries)

    final override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        loadAdditional(tag, lookupProvider)
    }

    final override fun onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket, lookupProvider: HolderLookup.Provider) {
        val tag: CompoundTag = pkt.tag
        if (!tag.isEmpty) handleUpdateTag(tag, lookupProvider)
    }

    fun sendUpdatePacket() {
        if (isRemote) return
        sendUpdatePacket(level as ServerLevel)
    }

    fun sendUpdatePacket(level: ServerLevel) {
        if (isRemoved) return
        PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos(blockPos), HTBlockEntityUpdatePacket(this))
    }

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        val writer: HTNbtCodec.Writer = object : HTNbtCodec.Writer {
            override fun <T : Any> write(codec: Codec<T>, key: String, value: T) {
                codec
                    .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), value)
                    .ifSuccess { tag.put(key, it) }
                    .ifError { error: DataResult.Error<Tag> -> LOGGER.error(error.message()) }
            }

            override fun write(key: String, serializable: INBTSerializable<CompoundTag>) {
                tag.put(key, serializable.serializeNBT(registries))
            }
        }
        // Custom Name
        writer.writeNullable(BiCodecs.TEXT, "custom_name", customName)
        // Custom
        writeNbt(writer)
    }

    final override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        val reader: HTNbtCodec.Reader = object : HTNbtCodec.Reader {
            override fun <T : Any> read(codec: Codec<T>, key: String): DataResult<T> = codec
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get(key))

            override fun read(key: String, serializable: INBTSerializable<CompoundTag>) {
                serializable.deserializeNBT(registries, tag.getCompound(key))
            }
        }
        // Custom Name
        reader.read(BiCodecs.TEXT, "custom_name").ifSuccess { customName = it }
        // Custom
        readNbt(reader)
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

    override fun onContentsChanged() {
        markOnlySave()
        reloadUpgrades()
    }

    protected fun markOnlySave() {
        setChanged(false)
    }

    override fun setChanged() {
        setChanged(true)
    }

    /**
     * @see [mekanism.common.tile.base.TileEntityUpdateable.setChanged]
     */
    protected fun setChanged(updateComparator: Boolean) {
        val level: Level = this.level ?: return
        if (level.isLoaded(blockPos)) {
            level.getChunkAt(blockPos).isUnsaved = true
        }
        if (updateComparator && !blockState.isAir) {
            level.updateNeighbourForOutputSignal(blockPos, blockState.block)
        }
    }

    //    Nameable    //

    private var customName: Component? = null

    override fun getName(): Component = customName ?: blockState.block.name

    override fun getCustomName(): Component? = customName
}
