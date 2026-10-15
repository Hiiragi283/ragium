package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.handler.HTInputHandler
import hiiragi283.lib.recipe.handler.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ALLOY_SMELTER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler<*, *, *> =
        object : SimpleRecipeHandler<HTRecipeInput, ItemStack, HTSizedShapelessRecipe>(RagiumRecipeLookups.ALLOYING) {
            private val inputSlots: HTInputHandler<Item, ItemInstance> by lazy {
                HTInputHandlerHelper.forItems(this@HTAlloySmelterBlockEntity.inputSlots)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTAlloySmelterBlockEntity.outputSlot)
            }

            override fun createProgressor(): Progressor<HTRecipeInput, HTSizedShapelessRecipe> = EnergizedProgressor()

            override fun createInput(): HTRecipeInput = HTRecipeInput(
                this@HTAlloySmelterBlockEntity.inputSlots.map(HTBasicItemSlot::getStackCopy),
                listOf(),
                customKeys
            )

            override fun canComplete(recipe: HTSizedShapelessRecipe, input: HTRecipeInput, output: ItemStack): Boolean =
                useTransaction { transaction: Transaction ->
                    when {
                        recipe.getMatchingStack(input).any { inputSlots.use(it, transaction).failed } -> false
                        else -> outputSlot.take(output, transaction).fullOrNoneTaken
                    }
                }

            override fun onComplete(recipe: HTSizedShapelessRecipe, input: HTRecipeInput, output: ItemStack) {
                useTransaction { transaction: Transaction ->
                    recipe.getMatchingStack(input).forEach { inputSlots.use(it, transaction) }
                    outputSlot.take(output, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.FIRE_EXTINGUISH)
            }
        }

    private lateinit var inputSlots: List<HTBasicItemSlot>
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlots = List(3) { builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener)) }
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(1.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        inputSlots.forEachIndexed { index: Int, slot: HTBasicItemSlot ->
            widgetHolder += HTItemWidget.Container(
                slot,
                0,
                HTSlotHelper.getSlotPosX(0.5 + index),
                HTSlotHelper.getSlotPosY(0.5),
                HTBackgroundType.INPUT
            )
            widgetHolder.track(slot)
        }
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            2,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(outputSlot)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.alloySmelter
}
