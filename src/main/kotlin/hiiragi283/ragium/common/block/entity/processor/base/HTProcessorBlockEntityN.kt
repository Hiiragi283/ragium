package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.lookup.HTRecipeLookupHandler
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.energy.HTBasicEnergyStorage
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTEnergyHandlerHolder
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.util.fixedFraction
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.gui.widget.HTProgressWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicEnergyHandlerHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTProcessorBlockEntityN(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBaseMachineBlockEntity(type, pos, state),
    HTRecipeLookupHandler {
    protected var progress: Int = 0
    protected var maxProgress: Int = 0

    val progression: Float get() = fixedFraction(progress, maxProgress)

    fun addProgressBar(widgetHolder: HTWidgetHolder, x: Int = HTSlotHelper.getSlotPosX(3.75), y: Int = HTSlotHelper.getSlotPosY(1)) {
        widgetHolder += HTProgressWidget(::progression, x, y)
    }

    protected fun updateProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
        progress = 0
    }

    protected var shouldCheck: Boolean = true
    protected var canProgress: Boolean = false

    override fun setOnlySave() {
        super.setOnlySave()
        shouldCheck = true
    }

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putInt(HTConstants.PROGRESS, progress)
        output.putInt(HTConstants.MAX_PROGRESS, maxProgress)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.getInt(HTConstants.MAX_PROGRESS).ifPresent { maxProgress: Int ->
            this.maxProgress = maxProgress
            input.getInt(HTConstants.PROGRESS).ifPresent { progress: Int ->
                this.progress = progress
            }
        }
    }

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        output.putInt(HTConstants.PROGRESS, progress)
        output.putInt(HTConstants.MAX_PROGRESS, maxProgress)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        input.getInt(HTConstants.MAX_PROGRESS).ifPresent { maxProgress: Int ->
            this.maxProgress = maxProgress
            input.getInt(HTConstants.PROGRESS).ifPresent { progress: Int ->
                this.progress = progress
            }
        }
    }

    final override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank>? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder, listener)
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {}

    final override fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot>? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder, listener)
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {}

    //    Energized    //

    abstract class Energized(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTProcessorBlockEntityN(type, pos, state) {
        protected lateinit var storage: HTBasicEnergyStorage
            private set

        override fun createEnergyStorage(listener: Runnable): HTEnergyHandlerHolder? {
            val builder: HTBasicEnergyHandlerHolder.Builder = HTBasicEnergyHandlerHolder.builder(this)
            storage = builder.addHandler(
                HTSlotInfo.INPUT,
                HTBasicEnergyStorage.input(
                    HTMachineUpgradeHelper.modifyCapacity(components(), getConfig().getCapacity()),
                    listener,
                ),
            )
            return builder.build()
        }

        protected abstract fun getConfig(): HTEnergyConfig

        fun addEnergySlot(widgetHolder: HTWidgetHolder, x: Int, y: Int) {
            widgetHolder += HTEnergySlotWidget(storage, x, y)
            widgetHolder.track(storage)
        }

        protected val baseEnergyPerTick: Int = getConfig().getUsage()
        protected val currentEnergyPerTick: Int
            get() = HTMachineUpgradeHelper.modifySpeed(components(), this.baseEnergyPerTick)

        protected fun getRecipeProgress(): Int = useTransaction { transaction: Transaction ->
            val extracted: Int = storage.extract(currentEnergyPerTick, transaction, HTTransferAccess.INTERNAL)
            if (extracted > 0) {
                transaction.commit()
            }
            extracted
        }

        protected open fun getMaxRecipeProgress(time: Int): Int = HTMachineUpgradeHelper.modifyEnergy(components(), time * this.baseEnergyPerTick)
    }
}
