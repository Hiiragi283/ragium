package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction
import java.util.function.IntSupplier

class HTElectrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ELECTROLYZER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler<*, *, *> =
        object : SimpleRecipeHandler<HTRecipeInput, RTElectrolyzingRecipe.Output, RTElectrolyzingRecipe>(
            RagiumRecipeLookups.ELECTROLYZING
        ) {
            private val inputTank: HTInputSlot<FluidInstance> by lazy {
                HTInputHandlerHelper.forFluid(this@HTElectrolyzerBlockEntity.inputTank)
            }

            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTElectrolyzerBlockEntity.outputSlot)
            }
            private val leftOutputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTElectrolyzerBlockEntity.leftOutputTank)
            }
            private val rightOutputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTElectrolyzerBlockEntity.rightOutputTank)
            }

            override fun createProgressor(): Progressor<HTRecipeInput, RTElectrolyzingRecipe> = EnergizedProgressor()

            override fun createInput(): HTRecipeInput = HTRecipeInput(inputTank.getStoredInput(), customKeys)

            override fun canComplete(
                recipe: RTElectrolyzingRecipe,
                input: HTRecipeInput,
                output: RTElectrolyzingRecipe.Output
            ): Boolean = useTransaction { transaction: Transaction ->
                val inputConsume: FluidInstance = recipe.getMatchingStack(input)
                when {
                    inputTank.use(inputConsume, transaction).failed -> false

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

            override fun onComplete(
                recipe: RTElectrolyzingRecipe,
                input: HTRecipeInput,
                output: RTElectrolyzingRecipe.Output
            ) {
                useTransaction { transaction: Transaction ->
                    val inputConsume: FluidInstance = recipe.getMatchingStack(input)
                    inputTank.use(inputConsume, transaction)
                    val (left: FluidStack, right: FluidStack, item: ItemStack) = output
                    outputSlot.take(item, transaction)
                    leftOutputTank.take(right, transaction)
                    rightOutputTank.take(left, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE)
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
            false
        )
        widgetHolder.track(inputTank)
        // outputs
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            0,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(outputSlot)
        widgetHolder += HTFluidWidget.Tank(
            leftOutputTank,
            HTSlotHelper.getSlotPosX(5.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false
        )
        widgetHolder.track(leftOutputTank)
        widgetHolder += HTFluidWidget.Tank(
            rightOutputTank,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.EXTRA_OUTPUT,
            false
        )
        widgetHolder.track(rightOutputTank)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.electrolyzer
}
