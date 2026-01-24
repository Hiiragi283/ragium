package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import com.lowdragmc.lowdraglib2.syncdata.annotation.RequireRerender
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.util.HTStackSlotHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.gui.HTEnergySlotElement
import hiiragi283.ragium.common.storge.energy.HTVariableEnergyBattery
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
    @RequireRerender
    val battery: HTEnergyBattery.Basic = createBattery()

    protected open fun createBattery(): HTEnergyBattery.Basic = HTVariableEnergyBattery.create {
        HTUpgradeHelper.getEnergyCapacity(this, RagiumConfig.COMMON.batteryCapacity.asInt)
    }

    final override fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder) {
        builder.addSlot(HTSlotInfo.BOTH, battery)
    }

    final override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(battery)

    override fun setupMainTab(root: UIElement) {
        root
            .addRowChild {
                alineCenter()
                addChild(HTEnergySlotElement().bind(battery))
            }
    }
}
