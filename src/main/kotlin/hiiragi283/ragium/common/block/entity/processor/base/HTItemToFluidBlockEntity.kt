package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
import hiiragi283.lib.recipe.handler.output.HTOutputHandlerHelper
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTItemToFluidBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTItemToBlockEntity.SingleOutput<FluidStack, HTItemToFluidRecipe>(type, pos, state) {
    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        createInputSlot(builder, listener)
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        // output
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputTank)
    }

    //    HTItemToBlockEntity    //

    override fun createOutputHandler(): HTOutputHandler<FluidStack> = HTOutputHandlerHelper.forFluid(outputTank)
}
