package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.HTSingleRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction
import java.util.function.IntSupplier

abstract class HTFluidSeparatingBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    protected fun createRecipeHandler(lookup: HTRecipeLookup<HTFluidSeparatingRecipe>, sound: HTSoundInstance): HTRecipeHandler =
        object : HTSingleRecipeHandler.Cached<FluidInstance, HTFluidSeparatingRecipe.Output, HTFluidSeparatingRecipe>(
            HTRecipeCacheHelper.SingleFluid(lookup),
        ) {
            private val inputTank: HTInputSlot<FluidInstance> by lazy {
                HTInputHandlerHelper.forFluid(this@HTFluidSeparatingBlockEntity.inputTank)
            }

            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTFluidSeparatingBlockEntity.outputSlot)
            }
            private val leftOutputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTFluidSeparatingBlockEntity.leftOutputTank)
            }
            private val rightOutputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTFluidSeparatingBlockEntity.rightOutputTank)
            }

            override fun createProgressor(): Progressor<FluidInstance, HTFluidSeparatingRecipe> = SingleProgressor()

            override fun getInput(): FluidInstance = inputTank.getStoredInput()

            override fun canComplete(recipe: HTFluidSeparatingRecipe, input: FluidInstance, output: HTFluidSeparatingRecipe.Output): Boolean =
                useTransaction { transaction: Transaction ->
                    when {
                        inputTank.use(recipe.getMatchingStack(input), transaction).failed -> false

                        else -> {
                            val (left: FluidStack, right: FluidStack, item: ItemStack) = output
                            when {
                                leftOutputTank.take(right, transaction) != HTOutputSlot.TakeResult.FULL -> false
                                rightOutputTank.take(left, transaction) != HTOutputSlot.TakeResult.FULL -> false
                                else -> outputSlot.take(item, transaction).fullOrNoneTaken
                            }
                        }
                    }
                }

            override fun onComplete(recipe: HTFluidSeparatingRecipe, input: FluidInstance, output: HTFluidSeparatingRecipe.Output) {
                useTransaction { transaction: Transaction ->
                    inputTank.use(recipe.getMatchingStack(input), transaction)
                    val (left: FluidStack, right: FluidStack, item: ItemStack) = output
                    outputSlot.take(item, transaction)
                    leftOutputTank.take(right, transaction)
                    rightOutputTank.take(left, transaction)
                    transaction.commit()
                }
                playSound(sound)
            }
        }

    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var leftOutputTank: HTBasicFluidTank
    private lateinit var rightOutputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        val capacity: IntSupplier = getTankCapacity()
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(capacity, listener))
        leftOutputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(capacity, listener))
        rightOutputTank = builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTVariableFluidTank.output(capacity, listener))
    }

    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.25), HTSlotHelper.getSlotPosY(2))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(2.25))
        // inputs
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        // outputs
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            0,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
        widgetHolder += HTFluidWidget.Tank(
            leftOutputTank,
            HTSlotHelper.getSlotPosX(5.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(leftOutputTank)
        widgetHolder += HTFluidWidget.Tank(
            rightOutputTank,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.EXTRA_OUTPUT,
            false,
        )
        widgetHolder.track(rightOutputTank)
    }
}
