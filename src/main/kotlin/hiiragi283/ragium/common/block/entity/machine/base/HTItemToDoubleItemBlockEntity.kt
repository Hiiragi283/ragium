package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.handler.HTInputSlot
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemToDoubleItemBlockEntity(
    type: BlockEntityType<*>,
    private val cache: HTRecipeCache<SingleRecipeInput, HTItemToDoubleItemRecipe>,
    pos: BlockPos,
    state: BlockState
) : HTProcessorBlockEntity.Energized(type, pos, state) {
    constructor(
        type: BlockEntityType<*>,
        lookup: HTRecipeLookup<HTItemToDoubleItemRecipe>,
        pos: BlockPos,
        state: BlockState
    ) : this(type, HTRecipeCache(lookup), pos, state)

    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        recipeHandler =
            object : EnergizedHandler<SingleRecipeInput, Pair<ItemStack, ItemStack>, HTItemToDoubleItemRecipe>() {
                private val inputSlot: HTInputSlot.SingleItem by lazy {
                    HTInputSlot.SingleItem(this@HTItemToDoubleItemBlockEntity.inputSlot)
                }
                private val primarySlot: HTOutputSlot<ItemStack> by lazy {
                    HTOutputSlot.SingleItem(this@HTItemToDoubleItemBlockEntity.primarySlot)
                }
                private val secondarySlot: HTOutputSlot<ItemStack> by lazy {
                    HTOutputSlot.SingleItem(this@HTItemToDoubleItemBlockEntity.secondarySlot)
                }

                override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

                override fun findRecipe(level: ServerLevel, input: SingleRecipeInput): HTItemToDoubleItemRecipe? =
                    cache.findFirstRecipe(input, level)

                override fun canComplete(
                    recipe: HTItemToDoubleItemRecipe,
                    input: SingleRecipeInput,
                    output: Pair<ItemStack, ItemStack>
                ): Boolean {
                    val inputCount: Int = recipe.getRequiredAmount(input.item())
                    return inputCount != 0 && useTransaction { transaction: Transaction ->
                        when {
                            !inputSlot.canExtract(inputCount, transaction) -> false
                            !primarySlot.canInsert(output.first, transaction) -> false
                            else -> secondarySlot.canInsert(output.second, transaction)
                        }
                    }
                }

                override fun onComplete(
                    recipe: HTItemToDoubleItemRecipe,
                    input: SingleRecipeInput,
                    output: Pair<ItemStack, ItemStack>
                ) {
                    val inputCount: Int = recipe.getRequiredAmount(input.item())
                    useTransaction { transaction: Transaction ->
                        if (inputCount > 0) {
                            inputSlot.extract(inputCount, transaction)
                        }
                        primarySlot.insert(output.first, transaction)
                        secondarySlot.insert(output.second, transaction)
                        transaction.commit()
                    }
                    playSound(getCompletedSound())
                }
            }
    }

    protected abstract fun getCompletedSound(): HTSoundInstance

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var primarySlot: HTBasicItemSlot
    private lateinit var secondarySlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        primarySlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        secondarySlot = builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTBasicItemSlot.output(listener))
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
            HTBackgroundType.INPUT
        )
        widgetHolder.track(inputSlot)
        // outputs
        widgetHolder += HTItemWidget.Container(
            primarySlot,
            1,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(primarySlot)
        widgetHolder += HTItemWidget.Container(
            secondarySlot,
            2,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.EXTRA_OUTPUT
        )
        widgetHolder.track(secondarySlot)
    }
}
