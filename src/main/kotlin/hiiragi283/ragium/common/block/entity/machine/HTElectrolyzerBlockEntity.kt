package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.HTFluidInputTank
import hiiragi283.lib.recipe.handler.HTItemInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.world.getTypedBlockEntity
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.storage.HTFluidOutputBusBlockEntity
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTElectrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ELECTROLYZER.get(), pos, state) {
    private val cache: HTRecipeCache<HTItemAndFluidRecipeInput, RTElectrolyzingRecipe> =
        HTRecipeCache(RagiumRecipeLookups.ELECTROLYZING)

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
        recipeHandler = object : EnergizedHandler<
            HTItemAndFluidRecipeInput,
            RTElectrolyzingRecipe.ElectrolyzedResult,
            RTElectrolyzingRecipe
            >() {
            private val machine: HTElectrolyzerBlockEntity = this@HTElectrolyzerBlockEntity
            private val front: Direction get() = machine.getFront()
            private val inputTank: HTFluidInputTank by lazy { HTFluidInputTank(machine.inputTank) }
            private val inputSlot: HTItemInputSlot by lazy { HTItemInputSlot(machine.inputSlot) }

            private val outputTank: HTOutputSlot<FluidStack> by lazy { HTOutputSlotHelper.forFluid(machine.outputTank) }
            private val rightOutputTank: HTOutputSlot<FluidStack>? get() = level
                ?.getTypedBlockEntity<HTFluidOutputBusBlockEntity>(blockPos.relative(front.clockWise))
                ?.tank
                ?.let(HTOutputSlotHelper::forFluid)
            private val leftOutputTank: HTOutputSlot<FluidStack>? get() = level
                ?.getTypedBlockEntity<HTFluidOutputBusBlockEntity>(blockPos.relative(front.counterClockWise))
                ?.tank
                ?.let(HTOutputSlotHelper::forFluid)

            override fun createInput(): HTItemAndFluidRecipeInput =
                HTItemAndFluidRecipeInput(inputSlot.getStoredInput(), inputTank.getStoredInput())

            override fun findRecipe(level: ServerLevel, input: HTItemAndFluidRecipeInput): RTElectrolyzingRecipe? =
                cache.findFirstRecipe(input, level)

            override fun canComplete(
                recipe: RTElectrolyzingRecipe,
                input: HTItemAndFluidRecipeInput,
                output: RTElectrolyzingRecipe.ElectrolyzedResult
            ): Boolean =
                rightOutputTank != null && leftOutputTank != null && useTransaction { transaction: Transaction ->
                    val (itemInput: ItemInstance, fluidInput: FluidInstance) =
                        recipe.getMatchingStack(input.item, input.fluid)
                    when {
                        inputSlot.use(itemInput, transaction).failed -> false

                        inputTank.use(fluidInput, transaction).failed -> false

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
                input: HTItemAndFluidRecipeInput,
                output: RTElectrolyzingRecipe.ElectrolyzedResult
            ) {
                useTransaction { transaction: Transaction ->
                    val (itemInput: ItemInstance, fluidInput: FluidInstance) =
                        recipe.getMatchingStack(input.item, input.fluid)
                    inputSlot.use(itemInput, transaction)
                    inputTank.use(fluidInput, transaction)
                    val (right: FluidStack, left: FluidStack, main: FluidStack) = output
                    outputTank.take(main, transaction)
                    rightOutputTank?.take(right, transaction)
                    leftOutputTank?.take(left, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE)
            }
        }
    }

    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
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
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
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
