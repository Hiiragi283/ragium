package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemOrFluidRecipe
import hiiragi283.lib.recipe.handler.HTDoubleRecipeHandler
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
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
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemOrFluidBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    fun createRecipeHandler(lookup: HTRecipeLookup<HTItemOrFluidRecipe>, sound: HTSoundInstance): HTRecipeHandler =
        object : HTDoubleRecipeHandler.Cached<ItemInstance, FluidInstance, HTItemAndFluidStack, HTItemOrFluidRecipe>(
            HTRecipeCacheHelper.ItemAndFluid(lookup, true)
        ) {
            private val inputTank: HTInputSlot<FluidInstance> by lazy {
                HTInputHandlerHelper.forFluid(this@HTItemOrFluidBlockEntity.inputTank)
            }
            private val inputSlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTItemOrFluidBlockEntity.inputSlot)
            }
            private val outputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTItemOrFluidBlockEntity.outputTank)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTItemOrFluidBlockEntity.outputSlot)
            }

            override fun createProgressor(): Progressor<ItemInstance, FluidInstance, HTItemOrFluidRecipe> =
                DoubleProgressor()

            override fun getFirstInput(): ItemInstance = inputSlot.getStoredInput()

            override fun getSecondInput(): FluidInstance = inputTank.getStoredInput()

            override fun canComplete(
                recipe: HTItemOrFluidRecipe,
                first: ItemInstance,
                second: FluidInstance,
                output: HTItemAndFluidStack
            ): Boolean = useTransaction { transaction: Transaction ->
                val (itemInput: ItemInstance, fluidInput: FluidInstance) = recipe.getMatchingStack(first, second)
                val (itemOutput: ItemStack, fluidOutput: FluidStack) = output
                when {
                    inputSlot.use(itemInput, transaction).failed -> false
                    inputTank.use(fluidInput, transaction).failed -> false
                    !outputSlot.take(itemOutput, transaction).fullOrNoneTaken -> false
                    else -> outputTank.take(fluidOutput, transaction).fullOrNoneTaken
                }
            }

            override fun onComplete(
                recipe: HTItemOrFluidRecipe,
                first: ItemInstance,
                second: FluidInstance,
                output: HTItemAndFluidStack
            ) {
                useTransaction { transaction: Transaction ->
                    val (itemInput: ItemInstance, fluidInput: FluidInstance) = recipe.getMatchingStack(first, second)
                    val (itemOutput: ItemStack, fluidOutput: FluidStack) = output
                    inputSlot.use(itemInput, transaction)
                    inputTank.use(fluidInput, transaction)
                    outputSlot.take(itemOutput, transaction)
                    outputTank.take(fluidOutput, transaction)
                    transaction.commit()
                }
                playSound(sound)
            }
        }

    private lateinit var inputTank: HTVariableFluidTank
    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
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
}
