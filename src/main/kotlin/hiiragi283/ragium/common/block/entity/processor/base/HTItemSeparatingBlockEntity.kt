package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
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
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemSeparatingBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTItemToBlockEntity<HTItemSeparatingRecipe.Output, HTItemSeparatingRecipe>(type, pos, state) {

    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    private lateinit var outputSlots: List<HTBasicItemSlot>

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        createInputSlot(builder, listener)
        outputSlots = List(4) { builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener)) }
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(2.75))
        // input
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        // outputs
        outputSlots.forEachIndexed { index: Int, slot: HTBasicItemSlot ->
            widgetHolder += HTItemWidget.Container(
                slot,
                1 + index,
                HTSlotHelper.getSlotPosX(5 + index % 2),
                HTSlotHelper.getSlotPosY(0.5 + index / 2),
                HTBackgroundType.OUTPUT,
            )
            widgetHolder.track(slot)
        }
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(7.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }

    //    HTItemToBlockEntity    //

    private val outputItemHandler: HTOutputSlot<ItemStack> = HTOutputSlotHelper.forItems(outputSlots)
    private val outputFluidHandler: HTOutputSlot<FluidStack> = HTOutputSlotHelper.forFluid(outputTank)

    override fun canComplete(recipe: HTItemSeparatingRecipe, input: ItemInstance, output: HTItemSeparatingRecipe.Output): Boolean =
        useTransaction { transaction: Transaction ->
            val (items: List<ItemStack>, fluid: FluidStack) = output
            when {
                inputHandler.use(recipe.getMatchingStack(input), transaction).failed -> false
                !outputFluidHandler.take(fluid, transaction).fullOrNoneTaken -> false
                else -> outputItemHandler.takeAll(items, transaction).fullOrNoneTaken
            }
        }

    override fun onComplete(recipe: HTItemSeparatingRecipe, input: ItemInstance, output: HTItemSeparatingRecipe.Output) {
        useTransaction { transaction: Transaction ->
            val (items: List<ItemStack>, fluid: FluidStack) = output
            inputHandler.use(recipe.getMatchingStack(input), transaction)
            outputItemHandler.takeAll(items, transaction)
            outputFluidHandler.take(fluid, transaction)
            transaction.commit()
        }
        onComplete()
    }
}
