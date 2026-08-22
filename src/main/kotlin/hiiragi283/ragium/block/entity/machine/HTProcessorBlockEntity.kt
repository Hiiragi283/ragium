package hiiragi283.ragium.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.sync.HTIntSyncSlot
import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.transfer.energy.HTMachineEnergyHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HTProcessorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTBaseMachineBlockEntity(type, pos, state) {

    //    Ticking    //

    fun modifyTime(time: Int): Int = time // modifyValue(HTUpgradeKeys.SPEED) { time / (it * getBaseMultiplier()) } TODO

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        TODO("Not yet implemented")
    }

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
            widgetHolder.track(HTIntSyncSlot.create(handler::amount), HTSyncType.S2C)
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
