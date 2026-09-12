package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction
import kotlin.jvm.optionals.getOrNull

class HTSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.SMELTER.get(), pos, state) {
    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        recipeHandler = object : EnergizedHandler<SingleRecipeInput, ItemStack, HTItemToItemRecipe>() {
            private val inputSlot: HTInputSlot.SingleItem by lazy {
                HTInputSlot.SingleItem(this@HTSmelterBlockEntity.inputSlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlot.SingleItem(this@HTSmelterBlockEntity.outputSlot)
            }

            override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

            override fun findRecipe(level: ServerLevel, input: SingleRecipeInput): HTItemToItemRecipe? {
                val stackIn: ItemStack = typeSlot.getStackCopy()
                val recipeType = when {
                    stackIn.`is`(Items.BLAST_FURNACE) -> RecipeType.BLASTING
                    stackIn.`is`(Items.SMOKER) -> RecipeType.SMOKING
                    else -> RecipeType.SMELTING
                }
                return level.recipeAccess()
                    .getRecipeFor(recipeType, input, level)
                    .map { it.value() }
                    .map(::wrapRecipe)
                    .getOrNull()
            }

            private fun wrapRecipe(recipe: AbstractCookingRecipe): HTItemToItemRecipe = object : HTItemToItemRecipe {
                override fun test(input: ItemInstance): Boolean =
                    HTIngredientHelper.unwrap(input).let(recipe.input()::test)

                override fun apply(input: ItemInstance): ItemStack =
                    HTIngredientHelper.unwrap(input).let(::SingleRecipeInput).let(recipe::assemble)

                override fun getRequiredAmount(input: ItemInstance): Int = when {
                    test(input) -> 1
                    else -> 0
                }

                override fun getProgressData(input: SingleRecipeInput): HTProgressData =
                    HTProgressData.time(recipe.cookingTime())
            }

            override fun canComplete(recipe: HTItemToItemRecipe, input: SingleRecipeInput, output: ItemStack): Boolean {
                val inputCount: Int = recipe.getRequiredAmount(input.item())
                return inputCount != 0 && useTransaction { transaction: Transaction ->
                    when {
                        !inputSlot.canExtract(inputCount, transaction) -> false
                        else -> outputSlot.canInsert(output, transaction)
                    }
                }
            }

            override fun onComplete(recipe: HTItemToItemRecipe, input: SingleRecipeInput, output: ItemStack) {
                val inputCount: Int = recipe.getRequiredAmount(input.item())
                useTransaction { transaction: Transaction ->
                    if (inputCount > 0) {
                        inputSlot.extract(inputCount, transaction)
                    }
                    outputSlot.insert(output, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.FIRE_EXTINGUISH)
            }
        }
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var typeSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        typeSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.input(listener))
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
            typeSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE
        )
        widgetHolder.track(typeSlot)
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

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.smelter
}
