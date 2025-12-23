package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.energy.HTBasicEnergyBattery
import hiiragi283.core.util.HTStackSlotHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

open class HTBatteryBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.BATTERY, pos, state)

    lateinit var battery: HTBasicEnergyBattery
        private set

    override fun initializeEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {
        battery = builder.addSlot(HTSlotInfo.BOTH, BatteryEnergyBattery(listener))
    }

    protected fun getCapacity(): Int = HTUpgradeHelper.getEnergyCapacity(this, RagiumConfig.COMMON.batteryCapacity.asInt)

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(battery)

    //    Ticking    //

    private var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val scale: Fraction = battery.getStoredLevel()
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }

    //    BatteryEnergyBattery    //

    protected inner class BatteryEnergyBattery(listener: HTContentListener) :
        HTBasicEnergyBattery(
            getCapacity(),
            HTStoragePredicates.alwaysTrue(),
            HTStoragePredicates.alwaysTrue(),
            listener,
        ) {
        private val isCreative: Boolean get() = this@HTBatteryBlockEntity.isCreative()

        override fun getAmount(): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> super.getAmount()
        }

        override fun getCapacity(): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> this@HTBatteryBlockEntity.getCapacity()
        }

        override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            val inserted: Int
            if (isCreative && this.getAmount() == 0 && action.execute() && access != HTStorageAccess.EXTERNAL) {
                inserted = super.insert(amount, HTStorageAction.SIMULATE, access)
                if (inserted == amount) {
                    setAmountUnchecked(getCapacity())
                }
            } else {
                inserted = super.insert(amount, action.combine(!isCreative), access)
            }
            return inserted
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
            super.extract(amount, action.combine(!isCreative), access)
    }
}
