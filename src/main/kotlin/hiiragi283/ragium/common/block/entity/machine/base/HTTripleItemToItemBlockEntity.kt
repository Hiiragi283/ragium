package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTTripleItemToItemRecipe
import hiiragi283.lib.recipe.handler.HTItemInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.input.HTItemListRecipeInput
import hiiragi283.lib.recipe.input.getItemOrEmpty
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
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTTripleItemToItemBlockEntity(
    type: BlockEntityType<*>,
    private val cache: HTRecipeCache<RecipeInput, HTTripleItemToItemRecipe>,
    pos: BlockPos,
    state: BlockState
) : HTProcessorBlockEntity.Energized(type, pos, state) {
    constructor(
        type: BlockEntityType<*>,
        lookup: HTRecipeLookup<HTTripleItemToItemRecipe>,
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
        recipeHandler = object : EnergizedHandler<RecipeInput, ItemStack, HTTripleItemToItemRecipe>() {
            private val primarySlot: HTItemInputSlot by lazy {
                HTItemInputSlot(this@HTTripleItemToItemBlockEntity.primarySlot)
            }
            private val secondarySlot: HTItemInputSlot by lazy {
                HTItemInputSlot(this@HTTripleItemToItemBlockEntity.secondarySlot)
            }
            private val tertiarySlot: HTItemInputSlot by lazy {
                HTItemInputSlot(this@HTTripleItemToItemBlockEntity.tertiarySlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlot.SingleItem(this@HTTripleItemToItemBlockEntity.outputSlot)
            }

            override fun createInput(): RecipeInput = HTItemListRecipeInput(
                primarySlot.getStoredInput(),
                secondarySlot.getStoredInput(),
                tertiarySlot.getStoredInput()
            )

            override fun findRecipe(level: ServerLevel, input: RecipeInput): HTTripleItemToItemRecipe? =
                cache.findFirstRecipe(input, level)

            override fun canComplete(recipe: HTTripleItemToItemRecipe, input: RecipeInput, output: ItemStack): Boolean {
                val (firstInput: ItemInstance, secondInput: ItemInstance, thirdInput: ItemInstance) =
                    recipe.getMatchingStack(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getItemOrEmpty(2))
                return useTransaction { transaction: Transaction ->
                    when {
                        primarySlot.use(firstInput, transaction).failed -> false
                        secondarySlot.use(secondInput, transaction).failed -> false
                        tertiarySlot.use(thirdInput, transaction).failed -> false
                        else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                    }
                }
            }

            override fun onComplete(recipe: HTTripleItemToItemRecipe, input: RecipeInput, output: ItemStack) {
                val (firstInput: ItemInstance, secondInput: ItemInstance, thirdInput: ItemInstance) =
                    recipe.getMatchingStack(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getItemOrEmpty(2))
                useTransaction { transaction: Transaction ->
                    primarySlot.use(firstInput, transaction)
                    secondarySlot.use(secondInput, transaction)
                    tertiarySlot.use(thirdInput, transaction)
                    outputSlot.take(output, transaction)
                    transaction.commit()
                }
                playSound(getCompletedSound())
            }
        }
    }

    protected abstract fun getCompletedSound(): HTSoundInstance

    private lateinit var primarySlot: HTBasicItemSlot
    private lateinit var secondarySlot: HTBasicItemSlot
    private lateinit var tertiarySlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        primarySlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        secondarySlot = builder.addSlot(
            HTSlotInfo.EXTRA_INPUT,
            HTBasicItemSlot.input(listener, filter = { resource: ItemResource ->
                tertiarySlot.resource.isEmpty || tertiarySlot.resource != resource
            })
        )
        tertiarySlot = builder.addSlot(
            HTSlotInfo.EXTRA_INPUT,
            HTBasicItemSlot.input(listener, filter = { resource: ItemResource ->
                secondarySlot.resource.isEmpty || secondarySlot.resource != resource
            })
        )
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(1.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            primarySlot,
            0,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT
        )
        widgetHolder.track(primarySlot)
        widgetHolder += HTItemWidget.Container(
            secondarySlot,
            1,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_INPUT
        )
        widgetHolder.track(secondarySlot)
        widgetHolder += HTItemWidget.Container(
            tertiarySlot,
            2,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_INPUT
        )
        widgetHolder.track(tertiarySlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            3,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(outputSlot)
    }
}
