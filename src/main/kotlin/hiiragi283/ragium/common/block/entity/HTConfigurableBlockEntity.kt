package hiiragi283.ragium.common.block.entity

import com.mojang.serialization.Codec
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTEnergyStorageHolder
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.ragium.common.transfer.holder.HTBasicEnergyStorageHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.common.transfer.holder.HTSlotInfoProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import thedarkcolour.kotlinforforge.neoforge.kotlin.enumMapOf

/**
 * 搬入出の面を制御可能な[HTBlockEntity]の拡張クラスです。
 *
 * 参考 : [Mekanism - TileEntityConfigurableMachine](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/tile/prefab/TileEntityConfigurableMachine.java)
 */
abstract class HTConfigurableBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state),
    HTSlotInfoProvider {
    companion object {
        @JvmStatic
        private val CONFIG_CODEC: Codec<Map<Direction, HTSlotInfo>> = HTCodecs.mapOf(Direction.CODEC, HTSlotInfo.CODEC)
    }

    private val slotInfoCache: MutableMap<Direction, HTSlotInfo> = enumMapOf()

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

    final override fun createEnergyStorage(listener: Runnable): HTEnergyStorageHolder? {
        val builder: HTBasicEnergyStorageHolder.Builder = HTBasicEnergyStorageHolder.builder(this)
        createEnergyStorage(builder, listener)
        return builder.build()
    }

    protected open fun createEnergyStorage(builder: HTBasicEnergyStorageHolder.Builder, listener: Runnable) {}

    final override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank>? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder, listener)
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {}

    final override fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot>? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder, listener)
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {}

    final override fun getSlotInfo(side: Direction): HTSlotInfo = slotInfoCache.getOrElse(side) { HTSlotInfo.BOTH }
}
