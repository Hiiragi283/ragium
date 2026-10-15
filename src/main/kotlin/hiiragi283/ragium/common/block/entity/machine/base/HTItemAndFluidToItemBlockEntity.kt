package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.handler.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.input.HTRecipeInput
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
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemAndFluidToItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    fun createRecipeHandler(
        lookup: HTRecipeLookup<HTItemAndFluidToItemRecipe>,
        sound: HTSoundInstance
    ): HTRecipeHandler<*, *, *> = object : SimpleRecipeHandler<
        HTRecipeInput,
        ItemStack,
        HTItemAndFluidToItemRecipe
        >(lookup) {
        private val inputTank: HTInputSlot<FluidInstance> by lazy {
            HTInputHandlerHelper.forFluid(this@HTItemAndFluidToItemBlockEntity.inputTank)
        }
        private val inputSlot: HTInputSlot<ItemInstance> by lazy {
            HTInputHandlerHelper.forItem(this@HTItemAndFluidToItemBlockEntity.inputSlot)
        }
        private val outputSlot: HTOutputSlot<ItemStack> by lazy {
            HTOutputSlotHelper.forItem(this@HTItemAndFluidToItemBlockEntity.outputSlot)
        }

        override fun createProgressor(): Progressor<HTRecipeInput, HTItemAndFluidToItemRecipe> = EnergizedProgressor()

        override fun createInput(): HTRecipeInput =
            HTRecipeInput(inputSlot.getStoredInput(), inputTank.getStoredInput(), customKeys)

        override fun canComplete(recipe: HTItemAndFluidToItemRecipe, input: HTRecipeInput, output: ItemStack): Boolean =
            useTransaction { transaction: Transaction ->
                val (itemInput: ItemInstance, fluidInput: FluidInstance) = recipe.getMatchingStack(input)
                when {
                    inputSlot.use(itemInput, transaction).failed -> false
                    inputTank.use(fluidInput, transaction).failed -> false
                    else -> outputSlot.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

        override fun onComplete(recipe: HTItemAndFluidToItemRecipe, input: HTRecipeInput, output: ItemStack) {
            useTransaction { transaction: Transaction ->
                val (itemInput: ItemInstance, fluidInput: FluidInstance) = recipe.getMatchingStack(input)
                inputSlot.use(itemInput, transaction)
                inputTank.use(fluidInput, transaction)
                outputSlot.take(output, transaction)
                transaction.commit()
            }
            playSound(sound)
        }
    }

    private lateinit var inputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
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
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT
        )
        widgetHolder.track(outputSlot)
    }
}
