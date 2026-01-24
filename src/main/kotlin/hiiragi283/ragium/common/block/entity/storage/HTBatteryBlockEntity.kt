package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.storge.energy.HTVariableEnergyBattery
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

open class HTBatteryBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTStorageBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.BATTERY, pos, state)

    lateinit var battery: HTEnergyBattery.Basic
        private set

    final override fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {
        battery = builder.addSlot(HTSlotInfo.BOTH, createBattery(listener))
    }

    protected open fun createBattery(listener: HTContentListener): HTEnergyBattery.Basic = HTVariableEnergyBattery.create(listener) {
        HTUpgradeHelper.getEnergyCapacity(this, RagiumConfig.COMMON.batteryCapacity.asInt)
    }

    final override fun getAmountView(): HTAmountView = battery
}
