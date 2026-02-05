package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.div
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemView
import hiiragi283.core.api.times
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

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

    fun modifyTime(time: Int): Int = modifyValue(HTUpgradeKeys.SPEED) { time / (it * getBaseMultiplier()) }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = recipeComponent.tick(level, pos)

    //    Extension    //

    fun createInput(view: HTItemView): SingleRecipeInput? {
        val resource: HTItemResourceType = view.getResource() ?: return null
        return SingleRecipeInput(resource.toStack(view.getAmount()))
    }

    fun createInput(view: HTFluidView): HTSingleFluidRecipeInput? {
        val resource: HTFluidResourceType = view.getResource() ?: return null
        return HTSingleFluidRecipeInput(resource.toStack(view.getAmount()))
    }

    fun createInput(itemView: HTItemView, fluidView: HTFluidView): HTItemAndFluidRecipeInput? {
        val item: HTItemResourceType = itemView.getResource() ?: return null
        val fluid: HTFluidResourceType = fluidView.getResource() ?: return null
        return HTItemAndFluidRecipeInput(item.toStack(itemView.getAmount()), fluid.toStack(itemView.getAmount()))
    }

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
