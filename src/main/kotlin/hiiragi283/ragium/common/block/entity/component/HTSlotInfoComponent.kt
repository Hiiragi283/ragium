package hiiragi283.ragium.common.block.entity.component

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.nextEntry
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.common.storge.holder.HTSlotInfoProvider
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import org.slf4j.Logger
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Function

/**
 * @see mekanism.common.tile.component.TileComponentConfig
 */
class HTSlotInfoComponent(owner: HTBlockEntity) :
    HTDataSerializable.CodecBased,
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

    override fun serialize(ops: RegistryOps<Tag>, consumer: BiConsumer<String, Tag>) {
        if (slotInfoCache.isEmpty()) return
        CONFIG_CODEC
            .encode(ops, slotInfoCache)
            .ifSuccess { consumer.accept(RagiumConst.SLOT_INFO, it) }
    }

    override fun deserialize(ops: RegistryOps<Tag>, function: Function<String, Tag>) {
        CONFIG_CODEC
            .decode(ops, function.apply(RagiumConst.SLOT_INFO))
            .ifSuccess(slotInfoCache::putAll)
    }

    //    HTSlotInfoProvider    //

    override fun getSlotInfo(side: Direction): HTSlotInfo = slotInfoCache.computeIfAbsent(side) { HTSlotInfo.BOTH }

    fun cycleSlotInfo(side: Direction) {
        val newSlot: HTSlotInfo = getSlotInfo(side).nextEntry()
        val oldSlot: HTSlotInfo? = slotInfoCache.put(side, newSlot)
        LOGGER.debug("Updated slot info for {} from {} to {}", side, oldSlot, newSlot)
    }
}
