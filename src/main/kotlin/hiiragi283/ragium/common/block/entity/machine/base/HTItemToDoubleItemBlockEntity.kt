package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.handler.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemToDoubleItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    fun createRecipeHandler(
        lookup: HTRecipeLookup<HTItemToDoubleItemRecipe>,
        sound: HTSoundInstance
    ): HTRecipeHandler<*, *, *> =
        object : SimpleRecipeHandler<HTRecipeInput, Pair<ItemStack, ItemStack>, HTItemToDoubleItemRecipe>(lookup) {
            private val inputSlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTItemToDoubleItemBlockEntity.inputSlot)
            }
            private val primarySlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItems(this@HTItemToDoubleItemBlockEntity.primarySlot)
            }
            private val secondarySlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItems(this@HTItemToDoubleItemBlockEntity.secondarySlot)
            }

            override fun createProgressor(): Progressor<HTRecipeInput, HTItemToDoubleItemRecipe> = EnergizedProgressor()

            override fun createInput(): HTRecipeInput = HTRecipeInput(inputSlot.getStoredInput(), customKeys)

            override fun canComplete(
                recipe: HTItemToDoubleItemRecipe,
                input: HTRecipeInput,
                output: Pair<ItemStack, ItemStack>
            ): Boolean = useTransaction { transaction: Transaction ->
                val inputConsume: ItemInstance = recipe.getMatchingStack(input)
                when {
                    inputSlot.use(inputConsume, transaction).failed -> false
                    primarySlot.take(output.first, transaction) != HTOutputSlot.TakeResult.FULL -> false
                    else -> secondarySlot.take(output.second, transaction).fullOrNoneTaken
                }
            }

            override fun onComplete(
                recipe: HTItemToDoubleItemRecipe,
                input: HTRecipeInput,
                output: Pair<ItemStack, ItemStack>
            ) {
                useTransaction { transaction: Transaction ->
                    inputSlot.use(recipe.getMatchingStack(input), transaction)
                    primarySlot.take(output.first, transaction)
                    secondarySlot.take(output.second, transaction)
                    transaction.commit()
                }
                playSound(sound)
            }
        }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var primarySlot: List<HTBasicItemSlot>
    private lateinit var secondarySlot: List<HTBasicItemSlot>

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        primarySlot = List(4) { builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener)) }
        secondarySlot = List(2) { builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTBasicItemSlot.output(listener)) }
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
            HTBackgroundType.INPUT
        )
        widgetHolder.track(inputSlot)
        // outputs
        primarySlot.forEachIndexed { index: Int, slot: HTBasicItemSlot ->
            widgetHolder += HTItemWidget.Container(
                slot,
                1 + index,
                HTSlotHelper.getSlotPosX(5 + index % 2),
                HTSlotHelper.getSlotPosY(0.5 + index / 2),
                HTBackgroundType.OUTPUT
            )
            widgetHolder.track(slot)
        }
        secondarySlot.forEachIndexed { index: Int, slot: HTBasicItemSlot ->
            widgetHolder += HTItemWidget.Container(
                slot,
                5 + index,
                HTSlotHelper.getSlotPosX(7.5),
                HTSlotHelper.getSlotPosY(0.5 + index % 2),
                HTBackgroundType.EXTRA_OUTPUT
            )
            widgetHolder.track(slot)
        }
    }
}
