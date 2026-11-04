package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.HTSingleRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.cache.HTSingleRecipeCache
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.lookup.HTSingleRecipeLookupHandler
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

abstract class HTItemToMultiItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state),
    HTSingleRecipeLookupHandler.ForItem<HTItemToMultiItemRecipe> {
    final override val recipeCache: HTSingleRecipeCache<ItemInstance, HTItemToMultiItemRecipe> = HTRecipeCacheHelper.SingleItem(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<HTItemToMultiItemRecipe>

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    protected fun createRecipeHandler(sound: HTSoundInstance): HTRecipeHandler =
        object : HTSingleRecipeHandler<ItemInstance, List<ItemStack>, HTItemToMultiItemRecipe>() {
            private val inputSlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTItemToMultiItemBlockEntity.inputSlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItems(this@HTItemToMultiItemBlockEntity.primarySlot)
            }

            override fun createProgressor(): Progressor<ItemInstance, HTItemToMultiItemRecipe> = SingleProgressor()

            override fun getInput(): ItemInstance = inputSlot.getStoredInput()

            override fun findRecipe(input: ItemInstance, level: ServerLevel): HTItemToMultiItemRecipe? = findFirstRecipe(input)

            override fun canComplete(recipe: HTItemToMultiItemRecipe, input: ItemInstance, output: List<ItemStack>): Boolean =
                useTransaction { transaction: Transaction ->
                    when {
                        inputSlot.use(recipe.getMatchingStack(input), transaction).failed -> false
                        else -> outputSlot.takeAll(output, transaction).fullOrNoneTaken
                    }
                }

            override fun onComplete(recipe: HTItemToMultiItemRecipe, input: ItemInstance, output: List<ItemStack>) {
                useTransaction { transaction: Transaction ->
                    inputSlot.use(recipe.getMatchingStack(input), transaction)
                    outputSlot.takeAll(output, transaction)
                    transaction.commit()
                }
                playSound(sound)
            }
        }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var primarySlot: List<HTBasicItemSlot>

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener, filter = ::hasRecipe))
        primarySlot = List(4) { builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener)) }
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
        primarySlot.forEachIndexed { index: Int, slot: HTBasicItemSlot ->
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
}
