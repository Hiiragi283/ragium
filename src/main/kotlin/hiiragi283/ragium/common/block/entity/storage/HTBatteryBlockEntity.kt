package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
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
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

open class HTBatteryBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.BATTERY, pos, state)

    @DescSynced
    @Persisted(subPersisted = true)
    val battery: HTBasicEnergyBattery = BatteryEnergyBattery()

    override fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder) {
        builder.addSlot(HTSlotInfo.BOTH, battery)
    }

    protected fun getCapacity(): Int = HTUpgradeHelper.getEnergyCapacity(this, RagiumConfig.COMMON.batteryCapacity.asInt)

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(battery)

    final override fun setupElements(root: UIElement) {}

    //    BatteryEnergyBattery    //

    protected inner class BatteryEnergyBattery :
        HTBasicEnergyBattery(
            getCapacity(),
            HTStoragePredicates.alwaysTrue(),
            HTStoragePredicates.alwaysTrue(),
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
