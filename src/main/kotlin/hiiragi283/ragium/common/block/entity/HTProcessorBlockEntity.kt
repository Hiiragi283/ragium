package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.common.gui.widget.HTProgressWidget
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.impl.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

abstract class HTProcessorBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state) {
    protected lateinit var recipeHandler: HTProgressHandler<*>
        private set
    protected lateinit var recipeComponent: HTRecipeComponent
        private set

    override fun initializeVariables() {
        super.initializeVariables()
        recipeHandler = createHandler()
        recipeComponent = HTRecipeComponent(this, recipeHandler)
    }

    protected abstract fun createHandler(): HTProgressHandler<*>

    fun addProgressBar(widgetHolder: HTWidgetHolder, x: Int = HTSlotHelper.getSlotPosX(4)) {
        widgetHolder += HTProgressWidget.createArrow(
            recipeComponent.fractionSlot,
            x,
            HTSlotHelper.getSlotPosY(1),
        )
    }

    final override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {}

    final override fun createEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? {
        val builder: HTBasicEnergyBatteryHolder.Builder = HTBasicEnergyBatteryHolder.builder(this)
        createEnergyBattery(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {}

    final override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {}

    //    Ticking    //

    fun modifyTime(time: Int): Int = time // modifyValue(HTUpgradeKeys.SPEED) { time / (it * getBaseMultiplier()) }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = recipeHandler.tick(level, pos)

    //    RecipeHandler    //

    abstract class RecipeHandler<RECIPE : Any, COMP : HTCompletedRecipe<RECIPE>> : HTProgressHandler<COMP>() {
        final override fun findRecipe(level: ServerLevel, pos: BlockPos): COMP? = findFirstRecipe(level, pos)?.let(::completeRecipe)

        protected abstract fun findFirstRecipe(level: ServerLevel, pos: BlockPos): RECIPE?

        protected abstract fun completeRecipe(recipe: RECIPE): COMP

        override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: COMP): Boolean = recipe.canComplete()
    }

    //    Energized    //

    abstract class Energized(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntity(type, pos, state) {
        lateinit var battery: HTMachineEnergyBattery.Processor
            private set

        final override fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {
            battery = builder.addSlot(HTSlotInfo.INPUT, HTMachineEnergyBattery.input(listener, this))
        }

        abstract fun getConfig(): HTEnergyConfig

        fun updateAndGetProgress(time: Int): Int {
            // if (isCreative()) return 0
            battery.currentEnergyPerTick = battery.baseEnergyPerTick
            // modifyValue(HTUpgradeKeys.ENERGY_EFFICIENCY) { battery.baseEnergyPerTick / it }
            return battery.currentEnergyPerTick * modifyTime(time)
        }

        //    ProgressHandler    //

        abstract inner class ProgressHandler<RECIPE : Any, COMP : HTCompletedRecipe.WithProgress<RECIPE>> : RecipeHandler<RECIPE, COMP>() {
            override fun getMaxProgress(recipe: COMP): Int = recipe
                .getProgress()
                .getProcessTime(battery.currentEnergyPerTick)
                .let(::updateAndGetProgress)

            final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = battery.consume()
        }
    }
}
