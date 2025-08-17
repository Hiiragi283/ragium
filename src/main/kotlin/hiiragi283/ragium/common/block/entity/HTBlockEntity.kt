package hiiragi283.ragium.common.block.entity

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.block.entity.HTBlockEntityExtension
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.INBTSerializable
import org.slf4j.Logger
import java.util.*

/**
 * Ragiumで使用する[BlockEntity]の拡張クラス
 */
abstract class HTBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTBlockEntityExtension,
    HTNbtCodec {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Save & Load    //

    /**
     * クライアント側へ同期する際に送る[CompoundTag]
     */
    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = saveCustomOnly(registries)

    /**
     * クライアント側でパケットを受け取った時の処理
     */
    final override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        loadAdditional(tag, lookupProvider)
    }

    override val upgrades: HTItemStackHandler = HTItemStackHandler.Builder(4).build(this)

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        val writer: HTNbtCodec.Writer = object : HTNbtCodec.Writer {
            override fun <T : Any> write(codec: Codec<T>, key: String, value: T) {
                codec
                    .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), value)
                    .ifSuccess { tag.put(key, it) }
                    .ifError { error: DataResult.Error<Tag> -> LOGGER.error(error.message()) }
            }

            override fun <T : Any> writeNullable(codec: Codec<T>, key: String, value: T?) {
                write(ExtraCodecs.optionalEmptyMap(codec), key, Optional.ofNullable(value))
            }

            override fun write(key: String, serializable: INBTSerializable<CompoundTag>) {
                tag.put(key, serializable.serializeNBT(registries))
            }
        }
        // Upgrades
        writer.write(RagiumConst.UPGRADES, upgrades)
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
        // Upgrades
        reader.read(RagiumConst.UPGRADES, upgrades)
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

    override fun onContentsChanged() {
        setChanged()
        reloadUpgrades()
    }
}
