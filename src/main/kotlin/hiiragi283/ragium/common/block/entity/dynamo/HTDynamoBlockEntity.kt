package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.lib.HTConstants
import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.energy.HTBasicEnergyHandler
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
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class HTDynamoBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTBaseMachineBlockEntity(type, pos, state) {
    // Energy
    protected lateinit var handler: HTBasicEnergyHandler
        private set

    override fun createEnergyHandler(listener: Runnable): HTEnergyHandlerHolder? {
        val builder: HTBasicEnergyHandlerHolder.Builder = HTBasicEnergyHandlerHolder.builder(this)
        handler = builder.addHandler(
            HTSlotInfo.OUTPUT,
            HTBasicEnergyHandler.output(
                HTMachineUpgradeHelper.modifyCapacity(components(), getConfig().getCapacity()),
                listener,
            ),
        )
        return builder.build()
    }

    protected abstract fun getConfig(): HTEnergyConfig

    protected val baseEnergyPerTick: Int = getConfig().getUsage()
    protected val currentEnergyPerTick: Int
        get() = HTMachineUpgradeHelper.modifySpeed(components(), this.baseEnergyPerTick)

    fun generateEnergy(transaction: TransactionContext): Int = handler.insert(currentEnergyPerTick, transaction, HTTransferAccess.INTERNAL)

    fun addEnergySlot(widgetHolder: HTWidgetHolder, x: Int, y: Int) {
        widgetHolder += HTEnergySlotWidget(handler, x, y)
        widgetHolder.track(handler)
    }

    protected fun exportEnergy(level: ServerLevel, pos: BlockPos) {
        for (direction: Direction in Direction.entries) {
            EnergyHandlerUtil.move(
                handler,
                HTEnergyCapabilities.getCapability(level, pos.relative(direction), direction.opposite),
                Int.MAX_VALUE,
                null,
            )
        }
    }

    // Fluid
    final override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank>? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder, listener)
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {}

    // Item
    final override fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot>? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder, listener)
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {}

    //    Progressed    //

    abstract class Progressed(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTDynamoBlockEntity(type, pos, state) {
        // Ticking
        protected var maxProgress: Int = 0
        protected var progress: Int = 0
        protected val progression: Float get() = fixedFraction(maxProgress - progress, maxProgress)

        fun addProgressBar(widgetHolder: HTWidgetHolder, x: Int = HTSlotHelper.getSlotPosX(3.75)) {
            widgetHolder += HTProgressWidget(::progression, x, HTSlotHelper.getSlotPosY(1))
        }

        override fun writeValue(output: ValueOutput) {
            super.writeValue(output)
            output.putInt(HTConstants.PROGRESS, progress)
            output.putInt(HTConstants.MAX_PROGRESS, maxProgress)
        }

        override fun readValue(input: ValueInput) {
            super.readValue(input)
            input.getInt(HTConstants.MAX_PROGRESS).ifPresent {
                this.maxProgress = it
                input.getInt(HTConstants.PROGRESS).ifPresent(::progress::set)
            }
        }

        override fun writeReducedUpdateTag(output: ValueOutput) {
            super.writeReducedUpdateTag(output)
            output.putInt(HTConstants.PROGRESS, progress)
            output.putInt(HTConstants.MAX_PROGRESS, maxProgress)
        }

        override fun readUpdateTag(input: ValueInput) {
            super.readUpdateTag(input)
            input.getInt(HTConstants.MAX_PROGRESS).ifPresent {
                this.maxProgress = it
                input.getInt(HTConstants.PROGRESS).ifPresent(::progress::set)
            }
        }

        override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
            // 周囲にエネルギーを自動搬出
            exportEnergy(level, pos)
            // 進捗が0の場合，燃焼時間を更新する
            if (progress <= 0) {
                useTransaction { transaction: Transaction ->
                    val maxProgress: Int = getMaxProgress(level, pos, state, transaction)
                    if (maxProgress > 0) {
                        this.maxProgress = maxProgress
                        this.progress = maxProgress
                        transaction.commit()
                    }
                }
            }
            // 進捗が0より大きい場合，発電を試みる
            if (progress > 0 && maxProgress > 0) {
                useTransaction { transaction: Transaction ->
                    val energyGenerated: Int = generateEnergy(level, pos, state, transaction)
                    if (energyGenerated > 0) {
                        progress--
                        transaction.commit()
                        return true
                    }
                }
            }
            return false
        }

        protected abstract fun getMaxProgress(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: Transaction): Int

        protected open fun generateEnergy(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: Transaction): Int =
            generateEnergy(transaction)
    }
}
