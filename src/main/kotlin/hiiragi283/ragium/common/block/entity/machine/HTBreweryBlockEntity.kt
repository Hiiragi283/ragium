package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.BREWERY.get(), pos, state) {
    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        recipeHandler = object : EnergizedHandler<HTItemAndFluidRecipeInput, FluidStack, HTItemAndFluidToFluidRecipe>() {
            private val cache: HTRecipeCache<HTItemAndFluidRecipeInput, HTItemAndFluidToFluidRecipe> = HTRecipeCache(RagiumRecipeLookups.BREWING)
            private val inputSlot: HTInputSlot.SingleItem by lazy { HTInputSlot.SingleItem(this@HTBreweryBlockEntity.inputSlot) }
            private val inputTank: HTInputSlot.SingleFluid by lazy { HTInputSlot.SingleFluid(this@HTBreweryBlockEntity.inputTank) }
            private val outputTank: HTOutputSlot<FluidStack> by lazy { HTOutputSlot.SingleFluid(this@HTBreweryBlockEntity.outputTank) }

            override fun createInput(): HTItemAndFluidRecipeInput = HTItemAndFluidRecipeInput(inputSlot.getStack(), inputTank.getStack())

            override fun findRecipe(level: ServerLevel, input: HTItemAndFluidRecipeInput): HTItemAndFluidToFluidRecipe? = cache.findFirstRecipe(input, level)

            override fun canComplete(recipe: HTItemAndFluidToFluidRecipe, input: HTItemAndFluidRecipeInput, output: FluidStack): Boolean {
                val (inputCount: Int, inputAmount: Int) = recipe.getRequiredAmount(input.item, input.fluid)
                return useTransaction { transaction: Transaction ->
                    when {
                        inputCount > 0 && !inputSlot.canExtract(inputCount, transaction) -> false
                        inputAmount > 0 && !inputTank.canExtract(inputAmount, transaction) -> false
                        else -> outputTank.canInsert(output, transaction)
                    }
                }
            }

            override fun onComplete(recipe: HTItemAndFluidToFluidRecipe, input: HTItemAndFluidRecipeInput, output: FluidStack) {
                val (inputCount: Int, inputAmount: Int) = recipe.getRequiredAmount(input.item, input.fluid)
                useTransaction { transaction: Transaction ->
                    if (inputCount > 0) {
                        inputSlot.extract(inputCount, transaction)
                    }
                    if (inputAmount > 0) {
                        inputTank.extract(inputAmount, transaction)
                    }
                    outputTank.insert(output, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.BREWING_STAND_BREW)
            }
        }
    }

    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTBasicFluidTank.input(getTankCapacity().asInt, listener))
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicFluidTank.output(getTankCapacity().asInt, listener))
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.brewery
}
