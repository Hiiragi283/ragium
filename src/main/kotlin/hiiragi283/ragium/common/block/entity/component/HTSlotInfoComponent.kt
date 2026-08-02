package hiiragi283.ragium.common.block.entity.component

import com.mojang.serialization.Codec
import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.collection.mutableEnumMapOf
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.component.DataComponentGetter
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import hiiragi283.ragium.support.storage.holder.HTSlotInfoProvider
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap

/**
 * @see mekanism.common.tile.component.TileComponentConfig
 */
class HTSlotInfoComponent(owner: HTBlockEntity) :
    HTBlockEntityComponent,
    HTSlotInfoProvider {
    companion object {
        @JvmStatic
        private val CONFIG_CODEC: Codec<Map<Direction, HTSlotInfo>> = HTCodecs.mapOf(Direction.CODEC, HTSlotInfo.CODEC)
    }

    init {
        owner.addComponent(this)
    }

    private val slotInfoCache: MutableMap<Direction, HTSlotInfo> = mutableEnumMapOf()

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

    override fun applyComponents(input: DataComponentGetter) {}

    override fun collectComponents(builder: DataComponentMap.Builder) {}

    //    HTSlotInfoProvider    //

    override fun getSlotInfo(side: Direction): HTSlotInfo = slotInfoCache.computeIfAbsent(side) { HTSlotInfo.BOTH }
}
