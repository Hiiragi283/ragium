package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTCatalysedItemToItemRecipe
import hiiragi283.lib.recipe.handler.HTDoubleRecipeHandler
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.lookup.HTDoubleRecipeLookupHandler
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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTCatalysedItemToItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state),
    HTDoubleRecipeLookupHandler<ItemInstance, ItemInstance, HTCatalysedItemToItemRecipe> {
    final override val recipeCache: HTRecipeCacheHelper.CatalysedItem<HTCatalysedItemToItemRecipe> = HTRecipeCacheHelper.CatalysedItem(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<HTCatalysedItemToItemRecipe>

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    protected fun createRecipeHandler(sound: HTSoundInstance): HTRecipeHandler = object :
        HTDoubleRecipeHandler<ItemInstance, ItemInstance, ItemStack, HTCatalysedItemToItemRecipe>() {
        private val inputSlot: HTInputSlot<ItemInstance> by lazy {
            HTInputHandlerHelper.forItem(this@HTCatalysedItemToItemBlockEntity.inputSlot)
        }
        private val catalystSlot: HTInputSlot<ItemInstance> by lazy {
            HTInputHandlerHelper.forItem(this@HTCatalysedItemToItemBlockEntity.catalystSlot)
        }
        private val outputSlot: HTOutputSlot<ItemStack> by lazy {
            HTOutputSlotHelper.forItem(this@HTCatalysedItemToItemBlockEntity.outputSlot)
        }

        override fun createProgressor(): Progressor<ItemInstance, ItemInstance, HTCatalysedItemToItemRecipe> = DoubleProgressor()

        override fun getFirstInput(): ItemInstance = inputSlot.getStoredInput()

        override fun getSecondInput(): ItemInstance = catalystSlot.getStoredInput()

        override fun findRecipe(first: ItemInstance, second: ItemInstance, level: ServerLevel): HTCatalysedItemToItemRecipe? =
            findFirstRecipe(first, second)

        override fun canComplete(recipe: HTCatalysedItemToItemRecipe, first: ItemInstance, second: ItemInstance, output: ItemStack): Boolean =
            useTransaction { transaction: Transaction ->
                when {
                    inputSlot.use(recipe.getMatchingStack(first), transaction).failed -> false
                    else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

        override fun onComplete(recipe: HTCatalysedItemToItemRecipe, first: ItemInstance, second: ItemInstance, output: ItemStack) {
            useTransaction { transaction: Transaction ->
                inputSlot.use(recipe.getMatchingStack(first), transaction)
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
        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.catalyst(listener))
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
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTItemWidget.Container(
            catalystSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(catalystSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            2,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }
}
