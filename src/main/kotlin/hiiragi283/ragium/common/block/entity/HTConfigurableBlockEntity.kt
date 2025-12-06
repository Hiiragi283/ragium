package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import hiiragi283.ragium.api.util.access.HTAccessConfigSetter
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * 搬入出の面を制御可能な[HTBlockEntity]の拡張クラス
 * @see mekanism.common.tile.prefab.TileEntityConfigurableMachine
 */
abstract class HTConfigurableBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(
        blockHolder,
        pos,
        state,
    ),
    HTAccessConfigGetter,
    HTAccessConfigSetter {
    companion object {
        @JvmStatic
        private val CONFIG_CODEC: BiCodec<ByteBuf, Map<Direction, HTAccessConfig>> =
            BiCodecs
                .mapOf(VanillaBiCodecs.DIRECTION, HTAccessConfig.CODEC)
                .validate { map: Map<Direction, HTAccessConfig> ->
                    if (map.isEmpty() || map.all { (_, config) -> config == HTAccessConfig.BOTH }) {
                        mapOf()
                    } else {
                        map
                    }
                }
    }

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        if (!accessConfigCache.isEmpty()) {
            output.store(RagiumConst.ACCESS_CONFIG, CONFIG_CODEC, accessConfigCache)
        }
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        input.read(RagiumConst.ACCESS_CONFIG, CONFIG_CODEC)?.forEach(accessConfigCache::put)
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

    private val accessConfigCache: MutableMap<Direction, HTAccessConfig> = hashMapOf()

    final override fun getAccessConfig(side: Direction): HTAccessConfig =
        accessConfigCache.computeIfAbsent(side) { _: Direction -> HTAccessConfig.BOTH }

    final override fun setAccessConfig(side: Direction, value: HTAccessConfig) {
        val old: HTAccessConfig? = accessConfigCache.put(side, value)
        RagiumAPI.LOGGER.debug("Updated access config: {} -> {}", old, value)
    }
}
