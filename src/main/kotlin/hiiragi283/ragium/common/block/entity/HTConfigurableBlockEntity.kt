package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.component.HTSlotInfoComponent
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.common.storge.holder.HTSlotInfoProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

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
    HTSlotInfoProvider {
    final override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder, listener)
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {}

    final override fun createEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? {
        val builder: HTBasicEnergyBatteryHolder.Builder = HTBasicEnergyBatteryHolder.builder(this)
        createEnergyBattery(builder, listener)
        return builder.build()
    }

    protected open fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {}

    final override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder, listener)
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {}

    //    HTSlotInfoProvider    //

    override fun initializeVariables() {
        super.initializeVariables()
        machineSlot = HTSlotInfoComponent(this)
    }

    lateinit var machineSlot: HTSlotInfoComponent
        private set

    final override fun getSlotInfo(side: Direction): HTSlotInfo = machineSlot.getSlotInfo(side)
}
