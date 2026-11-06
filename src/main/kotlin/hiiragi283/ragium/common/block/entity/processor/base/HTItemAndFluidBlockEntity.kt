package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemAndFluidRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemAndFluidBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTItemAndFluidToBlockEntity<HTItemAndFluidStack, HTItemAndFluidRecipe>(type, pos, state) {

    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        createInputTank(builder, listener)
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        createInputSlot(builder, listener)
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
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
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            1,
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

    //    HTDoubleProcessorBlockEntity    //

    private val outputFluidHandler: HTOutputSlot<FluidStack> = HTOutputSlotHelper.forFluid(outputTank)
    private val outputItemHandler: HTOutputSlot<ItemStack> = HTOutputSlotHelper.forItem(outputSlot)

    override fun canComplete(recipe: HTItemAndFluidRecipe, first: ItemInstance, second: FluidInstance, output: HTItemAndFluidStack): Boolean =
        useTransaction { transaction: Transaction ->
            val (itemInput: ItemInstance, fluidInput: FluidInstance) = recipe.getMatchingStack(first, second)
            val (itemOutput: ItemStack, fluidOutput: FluidStack) = output
            when {
                inputItemHandler.use(itemInput, transaction).failed -> false
                inputFluidHandler.use(fluidInput, transaction).failed -> false
                !outputItemHandler.take(itemOutput, transaction).fullOrNoneTaken -> false
                else -> outputFluidHandler.take(fluidOutput, transaction).fullOrNoneTaken
            }
        }

    override fun onComplete(recipe: HTItemAndFluidRecipe, first: ItemInstance, second: FluidInstance, output: HTItemAndFluidStack) {
        useTransaction { transaction: Transaction ->
            val (itemInput: ItemInstance, fluidInput: FluidInstance) = recipe.getMatchingStack(first, second)
            val (itemOutput: ItemStack, fluidOutput: FluidStack) = output
            inputItemHandler.use(itemInput, transaction)
            inputFluidHandler.use(fluidInput, transaction)
            outputItemHandler.take(itemOutput, transaction)
            outputFluidHandler.take(fluidOutput, transaction)
            transaction.commit()
        }
        onComplete()
    }
}
