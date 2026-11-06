package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTChemicalLaboBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.CHEMICAL_LABO.get(), pos, state) {

    private lateinit var inputTank: HTVariableFluidTank
    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var catalystSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.catalyst(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, y = HTSlotHelper.getSlotPosY(1.5))
        // inputs
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        // catalyst
        widgetHolder += HTItemWidget.Container(
            catalystSlot,
            1,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.NONE,
        )
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            2,
            HTSlotHelper.getSlotPosX(5.5),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        TODO("Not yet implemented")
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.chemicalLabo
}
