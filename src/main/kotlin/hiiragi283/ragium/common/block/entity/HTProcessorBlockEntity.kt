package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.div
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.times
import hiiragi283.core.common.gui.sync.HTIntSyncSlot
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

abstract class HTProcessorBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state) {
    protected lateinit var recipeComponent: HTRecipeComponent<*, *>
        private set

    override fun initializeVariables() {
        super.initializeVariables()
        recipeComponent = createRecipeComponent()
    }

    protected abstract fun createRecipeComponent(): HTRecipeComponent<*, *>

    //    Ticking    //

    override fun addMenuTrackers(holder: HTWidgetHolder) {
        super.addMenuTrackers(holder)
        // Progress
        holder.track(HTIntSyncSlot.create(recipeComponent::progress))
        holder.track(HTIntSyncSlot.create(recipeComponent::maxProgress))
    }

    fun getProgress(): Fraction = recipeComponent.getProgress(isActive())

    fun modifyTime(time: Int): Int = modifyValue(HTUpgradeKeys.SPEED) { time / (it * getBaseMultiplier()) }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = recipeComponent.tick(level, pos)

    //    Energized    //

    abstract class Energized(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntity(type, pos, state) {
        lateinit var battery: HTMachineEnergyBattery.Processor
            private set

        final override fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {
            battery = builder.addSlot(HTSlotInfo.INPUT, HTMachineEnergyBattery.input(listener, this))
        }

        fun updateAndGetProgress(time: Int): Int {
            if (isCreative()) return 0
            battery.currentEnergyPerTick = modifyValue(HTUpgradeKeys.ENERGY_EFFICIENCY) { battery.baseEnergyPerTick / it }
            return battery.currentEnergyPerTick * modifyTime(time)
        }
    }
}
