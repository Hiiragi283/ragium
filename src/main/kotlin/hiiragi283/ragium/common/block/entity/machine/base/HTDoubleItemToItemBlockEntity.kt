package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.handler.HTInputSlot
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTDoubleItemToItemBlockEntity(
    type: BlockEntityType<*>,
    private val cache: HTRecipeCache<RecipeInput, HTDoubleItemToItemRecipe>,
    pos: BlockPos,
    state: BlockState
) : HTProcessorBlockEntity.Energized(type, pos, state) {
    constructor(
        type: BlockEntityType<*>,
        lookup: HTRecipeLookup<HTDoubleItemToItemRecipe>,
        pos: BlockPos,
        state: BlockState
    ) : this(type, HTRecipeCache(lookup), pos, state)

    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        recipeHandler = object : EnergizedHandler<RecipeInput, ItemStack, HTDoubleItemToItemRecipe>() {
            private val primarySlot: HTInputSlot.SingleItem by lazy {
                HTInputSlot.SingleItem(this@HTDoubleItemToItemBlockEntity.primarySlot)
            }
            private val secondarySlot: HTInputSlot.SingleItem by lazy {
                HTInputSlot.SingleItem(this@HTDoubleItemToItemBlockEntity.secondarySlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlot.SingleItem(this@HTDoubleItemToItemBlockEntity.outputSlot)
            }

            override fun createInput(): RecipeInput =
                HTItemListRecipeInput(primarySlot.getStack(), secondarySlot.getStack())

            override fun findRecipe(level: ServerLevel, input: RecipeInput): HTDoubleItemToItemRecipe? =
                cache.findFirstRecipe(input, level)

            override fun canComplete(recipe: HTDoubleItemToItemRecipe, input: RecipeInput, output: ItemStack): Boolean {
                val (firstCount: Int, secondCount: Int) = recipe.getRequiredAmount(
                    input.getItemOrEmpty(0),
                    input.getItemOrEmpty(1)
                )
                useTransaction { transaction: Transaction ->
                    if (firstCount > 0 && !primarySlot.canExtract(firstCount, transaction)) {
                        return false
                    }
                    if (secondCount > 0 && !secondarySlot.canExtract(secondCount, transaction)) {
                        return false
                    }
                    return outputSlot.canInsert(output, transaction)
                }
            }

            override fun onComplete(recipe: HTDoubleItemToItemRecipe, input: RecipeInput, output: ItemStack) {
                val (firstCount: Int, secondCount: Int) = recipe.getRequiredAmount(
                    input.getItemOrEmpty(0),
                    input.getItemOrEmpty(1)
                )
                useTransaction { transaction: Transaction ->
                    if (firstCount > 0) {
                        primarySlot.extract(firstCount, transaction)
                    }
                    if (secondCount > 0) {
                        secondarySlot.extract(secondCount, transaction)
                    }
                    outputSlot.insert(output, transaction)
                    transaction.commit()
                }
                playSound(getCompletedSound())
            }
        }
    }

    protected abstract fun getCompletedSound(): HTSoundInstance

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
        widgetHolder += HTItemWidget.Container(
            primarySlot,
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
