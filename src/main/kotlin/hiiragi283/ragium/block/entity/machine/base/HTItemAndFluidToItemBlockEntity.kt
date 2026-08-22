package hiiragi283.ragium.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.gui.widget.HTFluidWidget
import hiiragi283.ragium.gui.widget.HTItemWidget
import hiiragi283.ragium.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemAndFluidToItemBlockEntity(type: BlockEntityType<*>, private val cache: HTRecipeCache<HTItemAndFluidRecipeInput, HTItemAndFluidToItemRecipe>, pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(type, pos, state) {
    constructor(type: BlockEntityType<*>, lookup: HTRecipeLookup<HTItemAndFluidToItemRecipe>, pos: BlockPos, state: BlockState) : this(type, HTRecipeCache(lookup), pos, state)

    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        recipeHandler = object : EnergizedHandler<HTItemAndFluidRecipeInput, ItemStack, HTItemAndFluidToItemRecipe>() {
            private val inputTank: HTInputSlot.SingleFluid by lazy { HTInputSlot.SingleFluid(this@HTItemAndFluidToItemBlockEntity.inputTank) }
            private val inputSlot: HTInputSlot.SingleItem by lazy { HTInputSlot.SingleItem(this@HTItemAndFluidToItemBlockEntity.inputSlot) }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy { HTOutputSlot.SingleItem(this@HTItemAndFluidToItemBlockEntity.outputSlot) }

            override fun createInput(): HTItemAndFluidRecipeInput = HTItemAndFluidRecipeInput(inputSlot.getStack(), inputTank.getStack())

            override fun findRecipe(level: ServerLevel, input: HTItemAndFluidRecipeInput): HTItemAndFluidToItemRecipe? = cache.findFirstRecipe(input, level)

            override fun canComplete(recipe: HTItemAndFluidToItemRecipe, input: HTItemAndFluidRecipeInput, output: ItemStack): Boolean {
                val (itemCount: Int, fluidAmount: Int) = recipe.getRequiredAmount(input.item, input.fluid)
                useTransaction { transaction: Transaction ->
                    if (itemCount > 0 && !inputSlot.canExtract(itemCount, transaction)) {
                        return false
                    }
                    if (fluidAmount > 0 && !inputTank.canExtract(fluidAmount, transaction)) {
                        return false
                    }
                    return outputSlot.canInsert(output, transaction)
                }
            }

            override fun onComplete(recipe: HTItemAndFluidToItemRecipe, input: HTItemAndFluidRecipeInput, output: ItemStack) {
                val (itemCount: Int, fluidAmount: Int) = recipe.getRequiredAmount(input.item, input.fluid)
                useTransaction { transaction: Transaction ->
                    if (itemCount > 0) {
                        inputSlot.extract(itemCount, transaction)
                    }
                    if (fluidAmount > 0) {
                        inputTank.extract(fluidAmount, transaction)
                    }
                    outputSlot.insert(output, transaction)
                    transaction.commit()
                }
                playSound(getCompletedSound())
            }
        }
    }

    protected abstract fun getCompletedSound(): HTSoundInstance

    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTBasicFluidTank.input(getTankCapacity().asInt, listener))
    }

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
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            1,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }
}
