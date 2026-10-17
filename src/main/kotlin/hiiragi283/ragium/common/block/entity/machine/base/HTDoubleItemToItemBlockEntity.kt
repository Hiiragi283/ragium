package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
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
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.gui.widget.HTTextureWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
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
    ): HTRecipeHandler<*, *, *> = object : CachedHandler<HTRecipeInput, ItemStack, HTDoubleItemToItemRecipe>(lookup) {
        private val primarySlot: HTInputSlot<ItemInstance> by lazy {
            HTInputHandlerHelper.forItem(this@HTDoubleItemToItemBlockEntity.primarySlot)
        }
        private val secondarySlot: HTInputSlot<ItemInstance> by lazy {
            HTInputHandlerHelper.forItem(this@HTDoubleItemToItemBlockEntity.secondarySlot)
        }
        private val outputSlot: HTOutputSlot<ItemStack> by lazy {
            HTOutputSlotHelper.forItem(this@HTDoubleItemToItemBlockEntity.outputSlot)
        }

        override fun createProgressor(): Progressor<HTRecipeInput, HTDoubleItemToItemRecipe> = EnergizedProgressor()

        override fun createInput(level: ServerLevel): HTRecipeInput = HTRecipeInput(
            listOf(primarySlot.getStoredInput(), secondarySlot.getStoredInput()),
            listOf(),
            customKeys
        )

        override fun canComplete(recipe: HTDoubleItemToItemRecipe, input: HTRecipeInput, output: ItemStack): Boolean {
            val (firstConsume: ItemInstance, secondConsume: ItemInstance) = recipe.getMatchingStack(input)
            return useTransaction { transaction: Transaction ->
                when {
                    primarySlot.use(firstConsume, transaction).failed -> false
                    secondarySlot.use(secondConsume, transaction).failed -> false
                    else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }
        }

        override fun onComplete(recipe: HTDoubleItemToItemRecipe, input: HTRecipeInput, output: ItemStack) {
            val (firstConsume: ItemInstance, secondConsume: ItemInstance) = recipe.getMatchingStack(input)
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
