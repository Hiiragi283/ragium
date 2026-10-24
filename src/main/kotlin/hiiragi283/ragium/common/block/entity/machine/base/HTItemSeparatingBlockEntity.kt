package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.HTSingleRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
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
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemSeparatingBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    protected fun createRecipeHandler(
        lookup: HTRecipeLookup<HTItemSeparatingRecipe>,
        sound: HTSoundInstance
    ): HTRecipeHandler =
        object : HTSingleRecipeHandler.Cached<ItemInstance, HTItemSeparatingRecipe.Output, HTItemSeparatingRecipe>(
            HTRecipeCacheHelper.SingleItem(lookup)
        ) {
            private val inputSlot: HTInputSlot<ItemInstance> by lazy {
                HTInputHandlerHelper.forItem(this@HTItemSeparatingBlockEntity.inputSlot)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItems(this@HTItemSeparatingBlockEntity.outputSlots)
            }
            private val outputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTItemSeparatingBlockEntity.outputTank)
            }

            override fun createProgressor(): Progressor<ItemInstance, HTItemSeparatingRecipe> = SingleProgressor()

            override fun getInput(): ItemInstance = inputSlot.getStoredInput()

            override fun canComplete(
                recipe: HTItemSeparatingRecipe,
                input: ItemInstance,
                output: HTItemSeparatingRecipe.Output
            ): Boolean = useTransaction { transaction: Transaction ->
                val (items: List<ItemStack>, fluid: FluidStack) = output
                when {
                    inputSlot.use(recipe.getMatchingStack(input), transaction).failed -> false
                    !outputTank.take(fluid, transaction).fullOrNoneTaken -> false
                    else -> outputSlot.takeAll(items, transaction).fullOrNoneTaken
                }
            }

            override fun onComplete(
                recipe: HTItemSeparatingRecipe,
                input: ItemInstance,
                output: HTItemSeparatingRecipe.Output
            ) {
                useTransaction { transaction: Transaction ->
                    val (items: List<ItemStack>, fluid: FluidStack) = output
                    inputSlot.use(recipe.getMatchingStack(input), transaction)
                    outputSlot.takeAll(items, transaction)
                    outputTank.take(fluid, transaction)
                    transaction.commit()
                }
                playSound(sound)
            }
        }

    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlots: List<HTBasicItemSlot>

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlots = List(4) { builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener)) }
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(2.75))
        // input
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT
        )
        widgetHolder.track(inputSlot)
        // outputs
        outputSlots.forEachIndexed { index: Int, slot: HTBasicItemSlot ->
            widgetHolder += HTItemWidget.Container(
                slot,
                1 + index,
                HTSlotHelper.getSlotPosX(5 + index % 2),
                HTSlotHelper.getSlotPosY(0.5 + index / 2),
                HTBackgroundType.OUTPUT
            )
            widgetHolder.track(slot)
        }
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(7.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false
        )
        widgetHolder.track(outputTank)
    }
}
