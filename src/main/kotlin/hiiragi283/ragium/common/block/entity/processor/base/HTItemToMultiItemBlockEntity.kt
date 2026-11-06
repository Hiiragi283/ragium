package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemToMultiItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTItemToBlockEntity<List<ItemStack>, HTItemToMultiItemRecipe>(type, pos, state) {

    private lateinit var outputSlots: List<HTBasicItemSlot>

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        createInputSlot(builder, listener)
        outputSlots = List(4) { builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener)) }
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        // outputs
        outputSlots.forEachIndexed { index: Int, slot: HTBasicItemSlot ->
            widgetHolder += HTItemWidget.Container(
                slot,
                1 + index,
                HTSlotHelper.getSlotPosX(6 + index % 2),
                HTSlotHelper.getSlotPosY(0.5 + index / 2),
                HTBackgroundType.OUTPUT,
            )
            widgetHolder.track(slot)
        }
    }

    //    HTItemToBlockEntity    //

    private val outputHandler: HTOutputSlot<ItemStack> = HTOutputSlotHelper.forItems(outputSlots)

    override fun canComplete(recipe: HTItemToMultiItemRecipe, input: ItemInstance, output: List<ItemStack>): Boolean =
        useTransaction { transaction: Transaction ->
            when {
                inputHandler.use(recipe.getMatchingStack(input), transaction).failed -> false
                else -> outputHandler.takeAll(output, transaction).fullOrNoneTaken
            }
        }

    override fun onComplete(recipe: HTItemToMultiItemRecipe, input: ItemInstance, output: List<ItemStack>) {
        useTransaction { transaction: Transaction ->
            inputHandler.use(recipe.getMatchingStack(input), transaction)
            outputHandler.takeAll(output, transaction)
            transaction.commit()
        }
        onComplete()
    }
}
