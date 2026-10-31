package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.entity.FuelValues
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTFuelDynamoBlockEntity(pos: BlockPos, state: BlockState) :
    HTDynamoBlockEntity.Progressed(RagiumBlockEntityTypes.FUEL_DYNAMO.get(), pos, state) {
    companion object {
        @JvmStatic
        private val ASH_RESOURCE: ItemResource by lazy { RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.ASH).asResourceOrEmpty() }

        const val MAX_ASH_TICKS: Int = 20 * 10
    }

    private var ashProgress: Int = 0

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putInt("ash_progress", ashProgress)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.getInt("ash_progress").ifPresent(::ashProgress::set)
    }

    override fun getMaxProgress(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: TransactionContext): Int {
        val burnTime: Int = fuelSlot.resource.toStack(1).getBurnTime(null, level.fuelValues())
        if (burnTime <= 0) return 0
        val extracted: Int = fuelSlot.extractSelf(1, transaction, HTTransferAccess.INTERNAL)
        return if (extracted == 1) burnTime else 0
    }

    override fun generateEnergy(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: TransactionContext): Int {
        if (ashProgress == MAX_ASH_TICKS) {
            val ashInserted: Int = ashSlot.insert(ASH_RESOURCE, 1, transaction, HTTransferAccess.INTERNAL)
            if (ashInserted != 1) return 0
        }
        return generateEnergy(transaction)
    }

    override fun onEnergyGenerated(level: ServerLevel, pos: BlockPos, state: BlockState) {
        if (ashProgress == MAX_ASH_TICKS) {
            ashProgress = 0
            playSound(SoundEvents.FIRECHARGE_USE)
        } else {
            ashProgress++
        }
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.dynamo.fuelDynamo

    private lateinit var fuelSlot: HTBasicItemSlot
    private lateinit var ashSlot: HTBasicItemSlot

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
        ashSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTItemWidget.Container(
            fuelSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(fuelSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            ashSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(ashSlot)
        // output
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1))
    }
}
