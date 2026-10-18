package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.HTConstants
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
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.gui.widget.HTTextureWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTDoubleItemToItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    protected fun createRecipeHandler(
        lookup: HTRecipeLookup<HTDoubleItemToItemRecipe>,
        sound: HTSoundInstance
    ): HTRecipeHandler =
        object : HTDoubleRecipeHandler.Cached<ItemInstance, ItemInstance, ItemStack, HTDoubleItemToItemRecipe>(
            HTRecipeCacheHelper.DoubleItem(lookup, true)
        ) {
            private val primarySlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTDoubleItemToItemBlockEntity.primarySlot)
            }
            private val secondarySlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTDoubleItemToItemBlockEntity.secondarySlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTDoubleItemToItemBlockEntity.outputSlot)
            }

            override fun createProgressor(): Progressor<ItemInstance, ItemInstance, HTDoubleItemToItemRecipe> {
                TODO("Not yet implemented")
            }

            override fun getFirstInput(): ItemInstance = primarySlot.getStoredInput()

            override fun getSecondInput(): ItemInstance = secondarySlot.getStoredInput()

            override fun canComplete(
                recipe: HTDoubleItemToItemRecipe,
                first: ItemInstance,
                second: ItemInstance,
                output: ItemStack
            ): Boolean = useTransaction { transaction: Transaction ->
                val (firstConsume: ItemInstance, secondConsume: ItemInstance) = recipe.getMatchingStack(first, second)
                when {
                    primarySlot.use(firstConsume, transaction).failed -> false
                    secondarySlot.use(secondConsume, transaction).failed -> false
                    else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

            override fun onComplete(
                recipe: HTDoubleItemToItemRecipe,
                first: ItemInstance,
                second: ItemInstance,
                output: ItemStack
            ) {
                val (firstConsume: ItemInstance, secondConsume: ItemInstance) = recipe.getMatchingStack(first, second)
                useTransaction { transaction: Transaction ->
                    primarySlot.use(firstConsume, transaction)
                    secondarySlot.use(secondConsume, transaction)
                    outputSlot.take(output, transaction)
                    transaction.commit()
                }
                playSound(sound)
            }
        }

    private lateinit var primarySlot: HTBasicItemSlot
    private lateinit var secondarySlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        primarySlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        secondarySlot = builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(1.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            primarySlot,
            0,
            HTSlotHelper.getSlotPosX(0.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT
        )
        widgetHolder.track(primarySlot)
        widgetHolder += HTTextureWidget(
            RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, "plus"),
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(0.5),
            18,
            18
        )
        widgetHolder += HTItemWidget.Container(
            secondarySlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.EXTRA_INPUT
        )
        widgetHolder.track(secondarySlot)
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
