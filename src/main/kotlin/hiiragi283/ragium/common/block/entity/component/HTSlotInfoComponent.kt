package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.nextEntry
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.serialization.component.HTComponentInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.common.storge.holder.HTSlotInfoProvider
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import java.util.*

/**
 * @see mekanism.common.tile.component.TileComponentConfig
 */
class HTSlotInfoComponent(owner: HTBlockEntity) :
    HTBlockEntityComponent,
    HTSlotInfoProvider {
    companion object {
        @JvmStatic
        private val CONFIG_CODEC: BiCodec<ByteBuf, Map<Direction, HTSlotInfo>> =
            BiCodecs.mapOf(VanillaBiCodecs.DIRECTION, HTSlotInfo.CODEC)
    }

    init {
        owner.addComponent(this)
    }

    private val slotInfoCache: MutableMap<Direction, HTSlotInfo> = EnumMap(Direction::class.java)

    //    HTBlockEntityComponent    //

    override fun serialize(output: HTValueOutput) {
        output.write(
            RagiumConst.SLOT_INFO,
            CONFIG_CODEC,
            slotInfoCache.filterNot { (_, info: HTSlotInfo) -> info == HTSlotInfo.BOTH },
        )
    }

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.SLOT_INFO, CONFIG_CODEC)?.let(slotInfoCache::putAll)
    }

    override fun applyComponents(input: HTComponentInput) {}

    override fun collectComponents(builder: DataComponentMap.Builder) {}

    //    HTSlotInfoProvider    //

    override fun getSlotInfo(side: Direction): HTSlotInfo = slotInfoCache.computeIfAbsent(side) { HTSlotInfo.BOTH }

    fun cycleSlotInfo(side: Direction) {
        val newSlot: HTSlotInfo = getSlotInfo(side).nextEntry()
        val oldSlot: HTSlotInfo? = slotInfoCache.put(side, newSlot)
        RagiumAPI.LOGGER.debug("Updated slot info for {} from {} to {}", side, oldSlot, newSlot)
    }
}
