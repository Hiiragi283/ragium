package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.handler.HTDoubleRecipeHandler
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
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

abstract class HTCatalysedItemToItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    protected fun createRecipeHandler(
        lookup: HTRecipeLookup<HTDoubleItemToItemRecipe>,
        sound: HTSoundInstance
    ): HTRecipeHandler = object :
        HTDoubleRecipeHandler.Cached<ItemInstance, ItemInstance, ItemStack, HTDoubleItemToItemRecipe>(
            HTRecipeCacheHelper.DoubleItem(lookup, false)
        ) {
        private val inputSlot: HTInputSlot<ItemInstance> by lazy {
            HTInputHandlerHelper.forItem(this@HTCatalysedItemToItemBlockEntity.inputSlot)
        }
        private val catalystSlot: HTInputSlot<ItemInstance> by lazy {
            HTInputHandlerHelper.forItem(this@HTCatalysedItemToItemBlockEntity.catalystSlot)
        }
        private val outputSlot: HTOutputSlot<ItemStack> by lazy {
            HTOutputSlotHelper.forItem(this@HTCatalysedItemToItemBlockEntity.outputSlot)
        }

        override fun createProgressor(): Progressor<ItemInstance, ItemInstance, HTDoubleItemToItemRecipe> =
            DoubleProgressor()

        override fun getFirstInput(): ItemInstance = inputSlot.getStoredInput()

        override fun getSecondInput(): ItemInstance = catalystSlot.getStoredInput()

        override fun canComplete(
            recipe: HTDoubleItemToItemRecipe,
            first: ItemInstance,
            second: ItemInstance,
            output: ItemStack
        ): Boolean = useTransaction { transaction: Transaction ->
            when {
                inputSlot.use(recipe.getMatchingStack(first, second).first, transaction).failed -> false
                else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
            }
        }

        override fun onComplete(
            recipe: HTDoubleItemToItemRecipe,
            first: ItemInstance,
            second: ItemInstance,
            output: ItemStack
        ) {
            useTransaction { transaction: Transaction ->
                inputSlot.use(recipe.getMatchingStack(first, second).first, transaction)
                outputSlot.take(output, transaction)
                transaction.commit()
            }
            playSound(sound)
        }
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var catalystSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.input(listener, limit = 1))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTItemWidget.Container(
            catalystSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE
        )
        widgetHolder.track(catalystSlot)
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
}
