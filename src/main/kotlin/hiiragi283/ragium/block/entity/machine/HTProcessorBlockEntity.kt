package hiiragi283.ragium.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.sync.HTIntSyncSlot
import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.gui.widget.HTProgressWidget
import hiiragi283.ragium.transfer.energy.HTMachineEnergyHandler
import hiiragi283.ragium.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.transfer.holder.HTBasicItemSlotHolder
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HTProcessorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTBaseMachineBlockEntity(type, pos, state) {
    protected lateinit var recipeHandler: HTRecipeHandler<*, *, *>

    final override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank>? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {}

    final override fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot>? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {}

    fun addProgressBar(widgetHolder: HTWidgetHolder, x: Int = HTSlotHelper.getSlotPosX(4)) {
        widgetHolder += HTProgressWidget.createArrow(recipeHandler::progression, x, HTSlotHelper.getSlotPosY(1))
    }

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        recipeHandler.serialize(output)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        recipeHandler.deserialize(input)
    }

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        recipeHandler.serialize(output)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        recipeHandler.deserialize(input)
    }

    //    Ticking    //

    fun modifyTime(time: Int): Int = time // modifyValue(HTUpgradeKeys.SPEED) { time / (it * getBaseMultiplier()) } TODO

    final override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = recipeHandler.tick(level)

    //    Energized    //

    abstract class Energized(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(type, pos, state) {
        lateinit var handler: HTMachineEnergyHandler.Processor
            private set

        override fun initializeVariables(listener: Runnable) {
            super.initializeVariables(listener)
            handler = HTMachineEnergyHandler.input(listener, this) // TODO
        }

        abstract fun getConfig(): HTEnergyConfig

        fun updateAndGetProgress(time: Int): Int {
            // if (isCreative()) return 0
            handler.currentEnergyPerTick = handler.baseEnergyPerTick
            // modifyValue(HTUpgradeKeys.ENERGY_EFFICIENCY) { battery.baseEnergyPerTick / it } TODO
            return handler.currentEnergyPerTick * modifyTime(time)
        }

        fun addEnergySlot(widgetHolder: HTWidgetHolder, x: Int, y: Int) {
            widgetHolder += HTEnergySlotWidget(handler, x, y)
            widgetHolder.track(HTIntSyncSlot.create(handler::amount, handler::setAmount), HTSyncType.S2C)
        }

        override fun writeValue(output: ValueOutput) {
            super.writeValue(output)
            output.putChild(HTConstants.ENERGY, handler)
        }

        override fun readValue(input: ValueInput) {
            super.readValue(input)
            input.readChild(HTConstants.ENERGY, handler)
        }
    }
}
