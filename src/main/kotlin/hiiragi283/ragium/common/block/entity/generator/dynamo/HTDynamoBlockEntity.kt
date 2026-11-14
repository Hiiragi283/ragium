package hiiragi283.ragium.common.block.entity.generator.dynamo

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.util.fixedFraction
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTProgressWidget
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class HTDynamoBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTGeneratorBlockEntity(type, pos, state) {
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
                    onEnergyGenerated(level, pos, state)
                    transaction.commit()
                    return true
                }
            }
        }
        return false
    }

    protected abstract fun getMaxProgress(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: TransactionContext): Int

    protected open fun generateEnergy(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: TransactionContext): Int =
        generateEnergy(transaction)

    protected open fun onEnergyGenerated(level: ServerLevel, pos: BlockPos, state: BlockState) {}
}
