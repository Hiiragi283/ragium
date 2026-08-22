package hiiragi283.ragium.block.entity.machine

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.gui.widget.HTFluidWidget
import hiiragi283.ragium.gui.widget.HTItemWidget
import hiiragi283.ragium.recipe.RagiumRecipeLookups
import hiiragi283.ragium.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MELTER.get(), pos, state) {
    override fun initializeVariables(listener: Runnable) {
        super.initializeVariables(listener)
        recipeHandler = object : EnergizedHandler<SingleRecipeInput, FluidStack, HTItemToFluidRecipe>() {
            private val cache: HTRecipeCache<SingleRecipeInput, HTItemToFluidRecipe> = HTRecipeCache(RagiumRecipeLookups.MELTING)
            private val inputSlot: HTInputSlot.SingleItem by lazy { HTInputSlot.SingleItem(this@HTMelterBlockEntity.inputSlot) }
            private val outputSlot: HTOutputSlot<FluidStack> by lazy { HTOutputSlot.SingleFluid(this@HTMelterBlockEntity.outputTank) }

            override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

            override fun findRecipe(level: ServerLevel, input: SingleRecipeInput): HTItemToFluidRecipe? = cache.findFirstRecipe(input, level)

            override fun canComplete(recipe: HTItemToFluidRecipe, input: SingleRecipeInput, output: FluidStack): Boolean {
                val inputCount: Int = recipe.getRequiredAmount(input.item())
                return inputCount != 0 && useTransaction { transaction: Transaction -> inputSlot.canExtract(inputCount, transaction) && outputSlot.canInsert(output, transaction) }
            }

            override fun onComplete(recipe: HTItemToFluidRecipe, input: SingleRecipeInput, output: FluidStack) {
                useTransaction { transaction: Transaction ->
                    inputSlot.extract(recipe.getRequiredAmount(input.item()), transaction)
                    outputSlot.insert(output, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.LAVA_POP)
            }
        }
    }

    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicFluidTank.output(getTankCapacity().asInt, listener))
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
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
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        // output
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.melter
}
