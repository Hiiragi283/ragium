package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.storage.energy.battery.HTBasicEnergyBattery
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

open class HTBatteryBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(blockHolder, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlocks.BATTERY, pos, state)

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
            HTPredicates.alwaysTrue(),
            HTPredicates.alwaysTrue(),
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
            if (isCreative && this.getAmount() == 0 && action.execute && access != HTStorageAccess.EXTERNAL) {
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
