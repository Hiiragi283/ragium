package hiiragi283.ragium.block.entity

import com.mojang.serialization.Codec
import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.ragium.transfer.holder.HTSlotInfo
import hiiragi283.ragium.transfer.holder.HTSlotInfoProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

/**
 * 搬入出の面を制御可能な[HTBlockEntity]の拡張クラス
 * @see mekanism.common.tile.prefab.TileEntityConfigurableMachine
 */
abstract class HTConfigurableBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state),
    HTSlotInfoProvider {
    companion object {
        @JvmStatic
        private val CONFIG_CODEC: Codec<Map<Direction, HTSlotInfo>> = HTCodecs.mapOf(Direction.CODEC, HTSlotInfo.CODEC)
    }

    private val slotInfoCache: MutableMap<Direction, HTSlotInfo> = mutableEnumMapOf()

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        if (slotInfoCache.isNotEmpty()) {
            output.store("slot_info", CONFIG_CODEC, slotInfoCache)
        }
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.read("slot_info", CONFIG_CODEC).ifPresent(slotInfoCache::putAll)
    }

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        if (slotInfoCache.isNotEmpty()) {
            output.store("slot_info", CONFIG_CODEC, slotInfoCache)
        }
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        input.read("slot_info", CONFIG_CODEC).ifPresent(slotInfoCache::putAll)
    }

    final override fun getSlotInfo(side: Direction): HTSlotInfo = slotInfoCache.getOrPut(side) { HTSlotInfo.BOTH }
}
