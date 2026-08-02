package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.common.gui.widget.HTProgressWidget
import hiiragi283.core.support.gui.sync.HTIntSyncSlot
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyHandler
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.support.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class HTProcessorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTMachineBlockEntity(type, pos, state) {
    protected lateinit var recipeHandler: HTProgressHandler<*>
        private set
    protected lateinit var recipeComponent: HTRecipeComponent
        private set

    override fun initializeVariables(listener: HTContentListener) {
        super.initializeVariables(listener)
        recipeHandler = createHandler()
        recipeComponent = HTRecipeComponent(this, recipeHandler)
    }

    protected abstract fun createHandler(): HTProgressHandler<*>

    fun addProgressBar(widgetHolder: HTWidgetHolder, x: Int = HTSlotHelper.getSlotPosX(4), vararg recipeTypes: HTRecipeViewerType<*>) {
        widgetHolder += HTProgressWidget.createArrow(
            recipeComponent.fractionSlot::amountAsFraction,
            x,
            HTSlotHelper.getSlotPosY(1),
        ).setSupportedRecipeTypes(*recipeTypes)
    }

    fun addProgressBar(widgetHolder: HTWidgetHolder, x: Int = HTSlotHelper.getSlotPosX(4), recipeTypes: Iterable<HTRecipeViewerType<*>>) {
        widgetHolder += HTProgressWidget.createArrow(
            recipeComponent.fractionSlot::amountAsFraction,
            x,
            HTSlotHelper.getSlotPosY(1),
        ).setSupportedRecipeTypes(recipeTypes)
    }

    final override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {}

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

    abstract class Energized(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(type, pos, state) {
        lateinit var handler: HTMachineEnergyHandler.Processor
            private set

        override fun initializeVariables(listener: HTContentListener) {
            super.initializeVariables(listener)
            handler = HTMachineEnergyHandler.input(recipeHandler.createListener(listener), this)
        }

        abstract fun getConfig(): HTEnergyConfig

        fun updateAndGetProgress(time: Int): Int {
            // if (isCreative()) return 0
            handler.currentEnergyPerTick = handler.baseEnergyPerTick
            // modifyValue(HTUpgradeKeys.ENERGY_EFFICIENCY) { battery.baseEnergyPerTick / it }
            return handler.currentEnergyPerTick * modifyTime(time)
        }

        fun addEnergySlot(widgetHolder: HTWidgetHolder, x: Int, y: Int) {
            widgetHolder += HTEnergySlotWidget(handler, x, y)
            widgetHolder.track(HTIntSyncSlot.create(handler), HTSyncType.S2C)
        }

        override fun writeValue(output: HTValueOutput) {
            super.writeValue(output)
            output.write(HTConst.ENERGY, handler)
        }

        override fun readValue(input: HTValueInput) {
            super.readValue(input)
            input.read(HTConst.ENERGY, handler)
            // migration
            input.child("batteries")?.read(HTConst.SLOT, handler)
        }

        //    ProgressHandler    //

        abstract inner class ProgressHandler<RECIPE : Any, COMP : HTCompletedRecipe.WithProgress<RECIPE>> : RecipeHandler<RECIPE, COMP>() {
            override fun getMaxProgress(recipe: COMP): Int = recipe
                .getProgress()
                .getProcessTime(handler.currentEnergyPerTick)
                .let(::updateAndGetProgress)

            final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = handler.consume()
        }
    }
}
