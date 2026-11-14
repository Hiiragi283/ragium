package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.energy.HTBasicEnergyStorage
import hiiragi283.lib.transfer.energy.HTEnergyStorageUtil
import hiiragi283.lib.transfer.holder.HTEnergyHandlerHolder
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicEnergyHandlerHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class HTGeneratorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTBaseMachineBlockEntity(type, pos, state) {
    // Energy
    protected lateinit var storage: HTBasicEnergyStorage
        private set

    final override fun createEnergyStorage(listener: Runnable): HTEnergyHandlerHolder? {
        val builder: HTBasicEnergyHandlerHolder.Builder = HTBasicEnergyHandlerHolder.builder(this)
        storage = builder.addHandler(
            HTSlotInfo.OUTPUT,
            HTBasicEnergyStorage.output(
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

    protected fun generateEnergy(transaction: TransactionContext): Int = storage.insert(currentEnergyPerTick, transaction, HTTransferAccess.INTERNAL)

    protected fun generateEnergy(transaction: TransactionContext, multiplier: Int): Int =
        storage.insert(currentEnergyPerTick * multiplier, transaction, HTTransferAccess.INTERNAL)

    protected fun addEnergySlot(widgetHolder: HTWidgetHolder, x: Int, y: Int) {
        widgetHolder += HTEnergySlotWidget(storage, x, y)
        widgetHolder.track(storage)
    }

    protected fun exportEnergy(level: ServerLevel, pos: BlockPos) {
        for (direction: Direction in Direction.entries) {
            HTEnergyStorageUtil.move(
                storage,
                HTEnergyCapabilities.getStorage(level, pos.relative(direction), direction.opposite),
                Int.MAX_VALUE,
                null,
            )
        }
    }
}
