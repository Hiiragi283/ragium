package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.FuelValues
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTFuelDynamoBlockEntity(pos: BlockPos, state: BlockState) :
    HTDynamoBlockEntity.Progressed(RagiumBlockEntityTypes.FUEL_DYNAMO.get(), pos, state) {
    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.dynamo.fuelDynamo

    override fun getMaxProgress(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: Transaction): Int {
        val burnTime: Int = fuelSlot.resource.toStack(1).getBurnTime(null, level.fuelValues())
        if (burnTime <= 0) return 0
        val extracted: Int = fuelSlot.extractSelf(1, transaction, HTTransferAccess.INTERNAL)
        return if (extracted == 1) burnTime else 0
    }

    private lateinit var fuelSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        fuelSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { resource: ItemResource ->
                    if (resource.isEmpty) return@input true
                    val fuelValue: FuelValues = this.level?.fuelValues() ?: return@input false
                    resource.toStack().getBurnTime(null, fuelValue) > 0
                },
            ),
        )
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            fuelSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(fuelSlot)
        // output
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1))
    }
}
