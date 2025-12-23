package hiiragi283.ragium.common.block.entity

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.access.HTAccessConfig
import hiiragi283.ragium.api.access.HTAccessConfigGetter
import hiiragi283.ragium.api.access.HTAccessConfigSetter
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import org.slf4j.Logger
import java.util.EnumMap

/**
 * 搬入出の面を制御可能な[HTBlockEntity]の拡張クラス
 * @see mekanism.common.tile.prefab.TileEntityConfigurableMachine
 */
abstract class HTConfigurableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(
        type,
        pos,
        state,
    ),
    HTAccessConfigGetter,
    HTAccessConfigSetter {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmStatic
        private val CONFIG_CODEC: BiCodec<ByteBuf, Map<Direction, HTAccessConfig>> =
            BiCodecs
                .mapOf(VanillaBiCodecs.DIRECTION, HTAccessConfig.CODEC)
                .validate { map: Map<Direction, HTAccessConfig> ->
                    if (map.isEmpty() || map.all { (_, config: HTAccessConfig) -> config == HTAccessConfig.BOTH }) {
                        mapOf()
                    } else {
                        map
                    }
                }
    }

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        if (!accessConfigCache.isEmpty()) {
            output.store(HTConst.ACCESS_CONFIG, CONFIG_CODEC, accessConfigCache)
        }
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        input.read(HTConst.ACCESS_CONFIG, CONFIG_CODEC)?.forEach(accessConfigCache::put)
    }

    final override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        initializeFluidTanks(builder, listener)
        return builder.build()
    }

    protected open fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {}

    final override fun initializeEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? {
        val builder: HTBasicEnergyBatteryHolder.Builder = HTBasicEnergyBatteryHolder.builder(this)
        initializeEnergyBattery(builder, listener)
        return builder.build()
    }

    protected open fun initializeEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {}

    final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        initializeItemSlots(builder, listener)
        return builder.build()
    }

    protected open fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {}

    //    HTAccessConfiguration    //

    private val accessConfigCache: MutableMap<Direction, HTAccessConfig> = EnumMap(Direction::class.java)

    final override fun getAccessConfig(side: Direction): HTAccessConfig =
        accessConfigCache.computeIfAbsent(side) { _: Direction -> HTAccessConfig.BOTH }

    final override fun setAccessConfig(side: Direction, value: HTAccessConfig) {
        val old: HTAccessConfig? = accessConfigCache.put(side, value)
        LOGGER.debug("Updated access config: {} -> {}", old, value)
    }
}
