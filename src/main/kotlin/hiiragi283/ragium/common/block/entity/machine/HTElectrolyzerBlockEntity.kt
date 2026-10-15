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
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.world.getTypedBlockEntity
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.storage.HTFluidOutputBusBlockEntity
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTElectrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ELECTROLYZER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler<*, *, *> =
        object : SimpleRecipeHandler<HTRecipeInput, RTElectrolyzingRecipe.Result, RTElectrolyzingRecipe>(
            RagiumRecipeLookups.ELECTROLYZING
        ) {
            private val machine: HTElectrolyzerBlockEntity = this@HTElectrolyzerBlockEntity
            private val front: Direction get() = machine.getFront()
            private val inputTank: HTInputSlot<FluidInstance> by lazy {
                HTInputHandlerHelper.forFluid(machine.inputTank)
            }

            private val outputTank: HTOutputSlot<FluidStack> by lazy { HTOutputSlotHelper.forFluid(machine.outputTank) }
            private val rightOutputTank: HTOutputSlot<FluidStack>? get() = level
                ?.getTypedBlockEntity<HTFluidOutputBusBlockEntity>(blockPos.relative(front.clockWise))
                ?.tank
                ?.let(HTOutputSlotHelper::forFluid)
            private val leftOutputTank: HTOutputSlot<FluidStack>? get() = level
                ?.getTypedBlockEntity<HTFluidOutputBusBlockEntity>(blockPos.relative(front.counterClockWise))
                ?.tank
                ?.let(HTOutputSlotHelper::forFluid)

            override fun createProgressor(): Progressor<HTRecipeInput, RTElectrolyzingRecipe> = EnergizedProgressor()

            override fun createInput(): HTRecipeInput = HTRecipeInput(inputTank.getStoredInput(), customKeys)

            override fun canComplete(
                recipe: RTElectrolyzingRecipe,
                input: HTRecipeInput,
                output: RTElectrolyzingRecipe.Result
            ): Boolean =
                rightOutputTank != null && leftOutputTank != null && useTransaction { transaction: Transaction ->
                    val inputConsume: FluidInstance = recipe.getMatchingStack(input)
                    when {
                        inputTank.use(inputConsume, transaction).failed -> false

                        else -> {
                            val (right: FluidStack, left: FluidStack, main: FluidStack) = output
                            when {
                                rightOutputTank!!.take(right, transaction) != HTOutputSlot.TakeResult.FULL -> false
                                leftOutputTank!!.take(left, transaction) != HTOutputSlot.TakeResult.FULL -> false
                                else -> outputTank.take(main, transaction).fullOrNoneTaken
                            }
                        }
                    }
                }

            override fun onComplete(
                recipe: RTElectrolyzingRecipe,
                input: HTRecipeInput,
                output: RTElectrolyzingRecipe.Result
            ) {
                useTransaction { transaction: Transaction ->
                    val inputConsume: FluidInstance = recipe.getMatchingStack(input)
                    inputTank.use(inputConsume, transaction)
                    val (right: FluidStack, left: FluidStack, main: FluidStack) = output
                    outputTank.take(main, transaction)
                    rightOutputTank?.take(right, transaction)
                    leftOutputTank?.take(left, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE)
            }
        }

    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(3.75), HTSlotHelper.getSlotPosY(2))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false
        )
        widgetHolder.track(inputTank)
        // output
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false
        )
        widgetHolder.track(outputTank)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.electrolyzer
}
