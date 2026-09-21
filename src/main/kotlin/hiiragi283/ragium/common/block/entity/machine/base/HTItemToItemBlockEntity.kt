package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.handler.HTItemInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.lookup.HTRecipeCache
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
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemToItemBlockEntity(
    type: BlockEntityType<*>,
    private val cache: HTRecipeCache<SingleRecipeInput, HTItemToItemRecipe>,
    pos: BlockPos,
    state: BlockState
) : HTProcessorBlockEntity.Energized(type, pos, state) {
    constructor(
        type: BlockEntityType<*>,
        lookup: HTRecipeLookup<HTItemToItemRecipe>,
        pos: BlockPos,
        state: BlockState
    ) : this(type, HTRecipeCache(lookup), pos, state)

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, cache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, cache)
    }

    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        recipeHandler = object : EnergizedHandler<SingleRecipeInput, ItemStack, HTItemToItemRecipe>() {
            private val inputSlot: HTItemInputSlot by lazy { HTItemInputSlot(this@HTItemToItemBlockEntity.inputSlot) }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlot.SingleItem(this@HTItemToItemBlockEntity.outputSlot)
            }

            override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputSlot.getStoredInput())

            override fun findRecipe(level: ServerLevel, input: SingleRecipeInput): HTItemToItemRecipe? =
                cache.findFirstRecipe(input, level)

            override fun canComplete(recipe: HTItemToItemRecipe, input: SingleRecipeInput, output: ItemStack): Boolean =
                useTransaction { transaction: Transaction ->
                    val inputConsume: ItemInstance = recipe.getMatchingStack(input.item())
                    when {
                        inputSlot.use(inputConsume, transaction).failed -> false
                        else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                    }
                }

            override fun onComplete(recipe: HTItemToItemRecipe, input: SingleRecipeInput, output: ItemStack) {
                useTransaction { transaction: Transaction ->
                    inputSlot.use(recipe.getMatchingStack(input.item()), transaction)
                    outputSlot.take(output, transaction)
                    transaction.commit()
                }
                playSound(getCompletedSound())
            }
        }
    }

    protected abstract fun getCompletedSound(): HTSoundInstance

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT
        )
        widgetHolder.track(inputSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            1,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(outputSlot)
    }
}
