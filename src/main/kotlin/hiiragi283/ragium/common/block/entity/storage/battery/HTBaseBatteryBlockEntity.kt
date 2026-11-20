package hiiragi283.ragium.common.block.entity.storage.battery

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.energy.HTEnergyStorage
import hiiragi283.lib.transfer.energy.HTEnergyStorageUtil
import hiiragi283.lib.transfer.item.HTEnergyItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTBlockEntityWithMenu
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.gui.widget.HTArrowWidget
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicEnergyStorageHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTBaseBatteryBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTBlockEntityWithMenu {
    abstract val storage: HTEnergyStorage

    abstract override fun createEnergyStorage(builder: HTBasicEnergyStorageHolder.Builder, listener: Runnable)

    protected fun getBatteryCapacity(): Int = HTMachineUpgradeHelper.modifyCapacity(components(), RagiumConfig.SERVER.batteryCapacity)

    private lateinit var fillSlot: HTEnergyItemSlot
    private lateinit var drainSlot: HTEnergyItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        fillSlot = builder.addSlot(HTSlotInfo.NONE, HTEnergyItemSlot.fill(storage, listener))
        drainSlot = builder.addSlot(HTSlotInfo.NONE, HTEnergyItemSlot.drain(storage, listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        widgetHolder += HTEnergySlotWidget(storage, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(1))
        // progress
        widgetHolder += HTArrowWidget.Right({ 0f }, HTSlotHelper.getSlotPosX(2.25), HTSlotHelper.getSlotPosY(1))
        widgetHolder += HTArrowWidget.Right({ 0f }, HTSlotHelper.getSlotPosX(5.25), HTSlotHelper.getSlotPosY(1))
        // inputs
        widgetHolder += HTItemWidget.Container(
            fillSlot,
            0,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(fillSlot)
        widgetHolder += HTItemWidget.Container(
            drainSlot,
            1,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(drainSlot)
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTEnergyStorageUtil.calculateRedstoneLevel(storage)

    //    Sync    //

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        storage.serialize(output)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        storage.deserialize(input)
    }

    //    Ticking    //

    private var oldScale: Float = 0f

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = moveEnergyInSlots() || updateScale()

    // スロット内のアイテムとバッテリーのストレージ間でエネルギーを移動する
    protected fun moveEnergyInSlots(): Boolean = useTransaction { transaction: Transaction ->
        val hasFilled: Boolean = fillSlot.fillContainerFromSlot(transaction)
        val hasDrained: Boolean = drainSlot.drainContainerToSlot(transaction)
        val moved: Boolean = hasDrained || hasFilled
        if (moved) transaction.commit()
        moved
    }

    // 保持する量の変化があれば更新させる
    protected fun updateScale(): Boolean {
        val scale: Float = storage.filledLevel
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }
}
