package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemAndFluidToFluidBlockEntity(
    type: BlockEntityType<*>,
    private val cache: HTRecipeCache<HTItemAndFluidRecipeInput, HTItemAndFluidToFluidRecipe>,
    pos: BlockPos,
    state: BlockState
) : HTProcessorBlockEntity.Energized(type, pos, state) {
    constructor(
        type: BlockEntityType<*>,
        lookup: HTRecipeLookup<HTItemAndFluidToFluidRecipe>,
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
        recipeHandler =
            object : EnergizedHandler<HTItemAndFluidRecipeInput, FluidStack, HTItemAndFluidToFluidRecipe>() {
                private val inputTank: HTInputSlot.SingleFluid by lazy {
                    HTInputSlot.SingleFluid(this@HTItemAndFluidToFluidBlockEntity.inputTank)
                }
                private val inputSlot: HTInputSlot.SingleItem by lazy {
                    HTInputSlot.SingleItem(this@HTItemAndFluidToFluidBlockEntity.inputSlot)
                }
                private val outputTank: HTOutputSlot<FluidStack> by lazy {
                    HTOutputSlot.SingleFluid(this@HTItemAndFluidToFluidBlockEntity.outputTank)
                }

                override fun createInput(): HTItemAndFluidRecipeInput =
                    HTItemAndFluidRecipeInput(inputSlot.getStack(), inputTank.getStack())

                override fun findRecipe(
                    level: ServerLevel,
                    input: HTItemAndFluidRecipeInput
                ): HTItemAndFluidToFluidRecipe? = cache.findFirstRecipe(input, level)

                override fun canComplete(
                    recipe: HTItemAndFluidToFluidRecipe,
                    input: HTItemAndFluidRecipeInput,
                    output: FluidStack
                ): Boolean {
                    val (itemCount: Int, fluidAmount: Int) = recipe.getRequiredAmount(input.item, input.fluid)
                    useTransaction { transaction: Transaction ->
                        if (itemCount > 0 && !inputSlot.canExtract(itemCount, transaction)) {
                            return false
                        }
                        if (fluidAmount > 0 && !inputTank.canExtract(fluidAmount, transaction)) {
                            return false
                        }
                        return outputTank.canInsert(output, transaction)
                    }
                }

                override fun onComplete(
                    recipe: HTItemAndFluidToFluidRecipe,
                    input: HTItemAndFluidRecipeInput,
                    output: FluidStack
                ) {
                    val (itemCount: Int, fluidAmount: Int) = recipe.getRequiredAmount(input.item, input.fluid)
                    useTransaction { transaction: Transaction ->
                        if (itemCount > 0) {
                            inputSlot.extract(itemCount, transaction)
                        }
                        if (fluidAmount > 0) {
                            inputTank.extract(fluidAmount, transaction)
                        }
                        outputTank.insert(output, transaction)
                        transaction.commit()
                    }
                    playSound(getCompletedSound())
                }
            }
    }

    protected abstract fun getCompletedSound(): HTSoundInstance

    private lateinit var inputTank: HTVariableFluidTank
    private lateinit var outputTank: HTVariableFluidTank

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
}
