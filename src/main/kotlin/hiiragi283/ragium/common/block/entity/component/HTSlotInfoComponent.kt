package hiiragi283.ragium.common.block.entity.component

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.common.storge.holder.HTSlotInfoProvider
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import org.slf4j.Logger
import java.util.*

/**
 * @see mekanism.common.tile.component.TileComponentConfig
 */
class HTSlotInfoComponent(owner: HTBlockEntity) :
    HTDataSerializable,
    HTSlotInfoProvider {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        val CONFIG_CODEC: BiCodec<ByteBuf, Map<Direction, HTSlotInfo>> = BiCodecs
            .mapOf(VanillaBiCodecs.DIRECTION, HTSlotInfo.CODEC)
            .validate { map: Map<Direction, HTSlotInfo> ->
                when {
                    map.isEmpty() -> mapOf()
                    map.all { (_, config: HTSlotInfo) -> config == HTSlotInfo.BOTH } -> mapOf()
                    else -> map
                }
            }
    }

    private val slotInfoCache: MutableMap<Direction, HTSlotInfo> = EnumMap(Direction::class.java)

    //    HTDataSerializable    //

    override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        if (slotInfoCache.isEmpty()) return
        val ops: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)
        CONFIG_CODEC
            .encode(ops, slotInfoCache)
            .ifSuccess { nbt.put(RagiumConst.SLOT_INFO, it) }
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        val ops: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)
        CONFIG_CODEC
            .decode(ops, nbt.getCompound(RagiumConst.SLOT_INFO))
            .ifSuccess(slotInfoCache::putAll)
    }

    //    HTSlotInfoProvider    //

    override fun getSlotInfo(side: Direction): HTSlotInfo = slotInfoCache.computeIfAbsent(side) { HTSlotInfo.BOTH }
}
