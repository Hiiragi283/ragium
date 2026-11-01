package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.HTSingleRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTEnchantmentWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTArcaneTransformerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity(RagiumBlockEntityTypes.ARCANE_TRANSFORMER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler = object : HTSingleRecipeHandler.Cached<ItemInstance, ItemStack, HTItemToItemRecipe>(
        HTRecipeCacheHelper.SingleItem(RagiumRecipeLookups.TRANSFORMING),
    ) {
        private val inputSlot: HTInputSlot<ItemInstance> by lazy { HTInputHandlerHelper.forItem(this@HTArcaneTransformerBlockEntity.inputSlot) }
        private val outputSlot: HTOutputSlot<ItemStack> by lazy { HTOutputSlotHelper.forItem(this@HTArcaneTransformerBlockEntity.outputSlot) }

        override fun createProgressor(): Progressor<ItemInstance, HTItemToItemRecipe> = object : Progressor<ItemInstance, HTItemToItemRecipe> {
            private fun collectPower(): Int = HTEnchantmentWidget.collectPower(level, blockPos).toInt()

            override fun getMaxProgress(recipe: HTItemToItemRecipe, input: ItemInstance): Int =
                recipe.getProgressData(input).getTotalEnergy(collectPower())

            override fun getProgress(): Int = collectPower()
        }

        override fun getInput(): ItemInstance = inputSlot.getStoredInput()

        override fun canComplete(recipe: HTItemToItemRecipe, input: ItemInstance, output: ItemStack): Boolean =
            useTransaction { transaction: Transaction ->
                when {
                    inputSlot.use(recipe.getMatchingStack(input), transaction).failed -> false
                    else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

        override fun onComplete(recipe: HTItemToItemRecipe, input: ItemInstance, output: ItemStack) {
            useTransaction { transaction: Transaction ->
                inputSlot.use(recipe.getMatchingStack(input), transaction)
                outputSlot.take(output, transaction)
                transaction.commit()
            }
            playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE)
        }
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnchantmentWidget(
            { HTEnchantmentWidget.collectPower(level, blockPos) },
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
        )
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            1,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }
}
