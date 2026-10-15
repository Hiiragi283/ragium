package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemToItemAndFluidRecipe
import hiiragi283.lib.recipe.handler.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
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
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTPyrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.PYROLYZER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler<*, *, *> =
        object : SimpleRecipeHandler<HTRecipeInput, HTItemAndFluidStack, HTItemToItemAndFluidRecipe>(
            RagiumRecipeLookups.PYROLYZING
        ) {
            private val inputSlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTPyrolyzerBlockEntity.inputSlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTPyrolyzerBlockEntity.outputSlot)
            }
            private val outputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTPyrolyzerBlockEntity.outputTank)
            }

            override fun createProgressor(): Progressor<HTRecipeInput, HTItemToItemAndFluidRecipe> =
                EnergizedProgressor()

            override fun createInput(): HTRecipeInput = HTRecipeInput(inputSlot.getStoredInput(), customKeys)

            override fun canComplete(
                recipe: HTItemToItemAndFluidRecipe,
                input: HTRecipeInput,
                output: HTItemAndFluidStack
            ): Boolean = useTransaction { transaction: Transaction ->
                val inputConsume: ItemInstance = recipe.getMatchingStack(input)
                val (item: ItemStack, fluid: FluidStack) = output
                when {
                    inputSlot.use(inputConsume, transaction).failed -> false
                    !outputSlot.take(item, transaction).fullOrNoneTaken -> false
                    else -> outputTank.take(fluid, transaction).fullOrNoneTaken
                }
            }

            override fun onComplete(
                recipe: HTItemToItemAndFluidRecipe,
                input: HTRecipeInput,
                output: HTItemAndFluidStack
            ) {
                useTransaction { transaction: Transaction ->
                    val inputConsume: ItemInstance = recipe.getMatchingStack(input)
                    val (item: ItemStack, fluid: FluidStack) = output
                    inputSlot.use(inputConsume, transaction)
                    outputSlot.take(item, transaction)
                    outputTank.take(fluid, transaction)
                    transaction.commit()
                }
                playSound(SoundEvents.BLAZE_DEATH)
            }
        }

    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
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
        // input
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT
        )
        widgetHolder.track(inputSlot)
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

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.pyrolyzer
}
