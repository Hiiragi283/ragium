package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
import hiiragi283.lib.recipe.handler.output.HTOutputHandlerHelper
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction
import java.util.function.IntSupplier

abstract class HTFluidSeparatingBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTFluidToBlockEntity<HTFluidSeparatingRecipe.Output, HTFluidSeparatingRecipe>(type, pos, state) {

    private lateinit var leftOutputTank: HTBasicFluidTank
    private lateinit var rightOutputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        val capacity: IntSupplier = getTankCapacity()
        createInputTank(builder, listener)
        leftOutputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(capacity, listener))
        rightOutputTank = builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTVariableFluidTank.output(capacity, listener))
    }

    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.25), HTSlotHelper.getSlotPosY(2))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(2.25))
        // inputs
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        // outputs
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            0,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
        widgetHolder += HTFluidWidget.Tank(
            leftOutputTank,
            HTSlotHelper.getSlotPosX(5.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(leftOutputTank)
        widgetHolder += HTFluidWidget.Tank(
            rightOutputTank,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.EXTRA_OUTPUT,
            false,
        )
        widgetHolder.track(rightOutputTank)
    }

    //    HTItemToBlockEntity    //

    private val outputItemHandler: HTOutputHandler<ItemStack> = HTOutputHandlerHelper.forItem(outputSlot)
    private val leftOutputHandler: HTOutputHandler<FluidStack> = HTOutputHandlerHelper.forFluid(leftOutputTank)
    private val rightOutputHandler: HTOutputHandler<FluidStack> = HTOutputHandlerHelper.forFluid(rightOutputTank)

    override fun canComplete(recipe: HTFluidSeparatingRecipe, input: FluidInstance, output: HTFluidSeparatingRecipe.Output): Boolean =
        useTransaction { transaction: Transaction ->
            when {
                inputHandler.use(recipe.getMatchingStack(input), transaction).failed -> false

                else -> {
                    val (left: FluidStack, right: FluidStack, item: ItemStack) = output
                    when {
                        leftOutputHandler.take(right, transaction) != HTOutputHandler.TakeResult.FULL -> false
                        rightOutputHandler.take(left, transaction) != HTOutputHandler.TakeResult.FULL -> false
                        else -> outputItemHandler.take(item, transaction).fullOrNoneTaken
                    }
                }
            }
        }

    override fun onComplete(recipe: HTFluidSeparatingRecipe, input: FluidInstance, output: HTFluidSeparatingRecipe.Output) {
        useTransaction { transaction: Transaction ->
            inputHandler.use(recipe.getMatchingStack(input), transaction)
            val (left: FluidStack, right: FluidStack, item: ItemStack) = output
            outputItemHandler.take(item, transaction)
            leftOutputHandler.take(right, transaction)
            rightOutputHandler.take(left, transaction)
            transaction.commit()
        }
        onComplete()
    }
}
