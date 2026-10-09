package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTFluidToItemAndFluidRecipe
import hiiragi283.lib.recipe.handler.HTFluidInputTank
import hiiragi283.lib.recipe.handler.HTItemInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.REFINERY.get(), pos, state) {
    private val cache: HTRecipeCache<HTSingleFluidRecipeInput, HTFluidToItemAndFluidRecipe> =
        HTRecipeCache(RagiumRecipeLookups.REFINING)

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
        recipeHandler = object: EnergizedHandler<HTSingleFluidRecipeInput, HTItemAndFluidStack, HTFluidToItemAndFluidRecipe>() {
            private val inputTank: HTFluidInputTank by lazy { HTFluidInputTank(this@HTRefineryBlockEntity.inputTank) }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTRefineryBlockEntity.outputSlot)
            }
            private val outputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTRefineryBlockEntity.outputTank)
            }
            
            override fun createInput(): HTSingleFluidRecipeInput = HTSingleFluidRecipeInput(inputTank.getStoredInput())

            override fun findRecipe(level: ServerLevel, input: HTSingleFluidRecipeInput): HTFluidToItemAndFluidRecipe? =
                cache.findFirstRecipe(input, level)

            override fun canComplete(
                recipe: HTFluidToItemAndFluidRecipe,
                input: HTSingleFluidRecipeInput,
                output: HTItemAndFluidStack
            ): Boolean = useTransaction { transaction: Transaction ->
                val inputConsume: FluidInstance = recipe.getMatchingStack(input)
                val (item: ItemStack, fluid: FluidStack) = output
                when {
                    inputTank.use(inputConsume, transaction).failed -> false
                    !outputSlot.take(item, transaction).fullOrNoneTaken -> false
                    else -> outputTank.take(fluid, transaction).fullOrNoneTaken
                }
            }

            override fun onComplete(
                recipe: HTFluidToItemAndFluidRecipe,
                input: HTSingleFluidRecipeInput,
                output: HTItemAndFluidStack
            ) {
                useTransaction { transaction: Transaction ->
                    val inputConsume: FluidInstance = recipe.getMatchingStack(input)
                    val (item: ItemStack, fluid: FluidStack) = output
                    inputTank.use(inputConsume, transaction)
                    outputSlot.take(item, transaction)
                    outputTank.take(fluid, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.FIRE_EXTINGUISH)
            }
        }
    }

    private lateinit var inputTank: HTVariableFluidTank
    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }
    
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false
        )
        widgetHolder.track(inputTank)
        // outputs
        // outputs
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            1,
            HTSlotHelper.getSlotPosX(5.5),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(outputSlot)
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false
        )
        widgetHolder.track(outputTank)
    }
    
    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.refinery
}
