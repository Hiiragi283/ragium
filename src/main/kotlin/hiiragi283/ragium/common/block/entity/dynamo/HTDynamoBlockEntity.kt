package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.energy.HTBasicEnergyHandler
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTEnergyHandlerHolder
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicEnergyHandlerHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
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
}
