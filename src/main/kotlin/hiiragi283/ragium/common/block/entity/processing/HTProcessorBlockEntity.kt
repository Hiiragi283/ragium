package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.math.div
import hiiragi283.core.api.math.times
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
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
    protected val recipeComponent: HTRecipeComponent<*, *> = createRecipeComponent()

    protected abstract fun createRecipeComponent(): HTRecipeComponent<*, *>

    fun getProgress(): Fraction = recipeComponent.getProgress(isActive())

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = recipeComponent.tick(level, pos)

    //    Energized    //

    abstract class Energized(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntity(type, pos, state) {
        lateinit var battery: HTMachineEnergyBattery.Processor
            protected set

        final override fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder) {
            battery = builder.addSlot(HTSlotInfo.INPUT, HTMachineEnergyBattery.input(this))
        }

        fun updateAndGetProgress(time: Int): Int {
            if (isCreative()) return 0
            battery.currentEnergyPerTick = modifyValue(HTUpgradeKeys.ENERGY_EFFICIENCY) { battery.baseEnergyPerTick / it }
            val time: Int = modifyValue(HTUpgradeKeys.SPEED) { time / (it * getBaseMultiplier()) }
            return battery.currentEnergyPerTick * time
        }
    }
}
