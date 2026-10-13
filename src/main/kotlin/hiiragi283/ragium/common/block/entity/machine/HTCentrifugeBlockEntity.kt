package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.HTItemInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.recipe.RTCentrifugingRecipe
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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTCentrifugeBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.CENTRIFUGE.get(), pos, state) {
    private val cache: HTRecipeCache<HTRecipeInput, RTCentrifugingRecipe> =
        HTRecipeCache(RagiumRecipeLookups.CENTRIFUGING)

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
        recipeHandler = object : EnergizedHandler<HTRecipeInput, RTCentrifugingRecipe.Output, RTCentrifugingRecipe>() {
            private val inputSlot: HTItemInputSlot by lazy { HTItemInputSlot(this@HTCentrifugeBlockEntity.inputSlot) }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItems(this@HTCentrifugeBlockEntity.outputSlots)
            }
            private val outputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTCentrifugeBlockEntity.outputTank)
            }

            override fun createInput(): HTRecipeInput = HTRecipeInput(inputSlot.getStoredInput(), customKeys)

            override fun findRecipe(level: ServerLevel, input: HTRecipeInput): RTCentrifugingRecipe? =
                cache.findFirstRecipe(input, level)

            override fun canComplete(
                recipe: RTCentrifugingRecipe,
                input: HTRecipeInput,
                output: RTCentrifugingRecipe.Output
            ): Boolean = useTransaction { transaction: Transaction ->
                val inputConsume: ItemInstance = recipe.getMatchingStack(input)
                val (items: List<ItemStack>, fluid: FluidStack) = output
                when {
                    inputSlot.use(inputConsume, transaction).failed -> false
                    !outputTank.take(fluid, transaction).fullOrNoneTaken -> false
                    else -> items.all { outputSlot.take(it, transaction).fullOrNoneTaken }
                }
            }

            override fun onComplete(
                recipe: RTCentrifugingRecipe,
                input: HTRecipeInput,
                output: RTCentrifugingRecipe.Output
            ) {
                useTransaction { transaction: Transaction ->
                    val inputConsume: ItemInstance = recipe.getMatchingStack(input)
                    val (items: List<ItemStack>, fluid: FluidStack) = output
                    inputSlot.use(inputConsume, transaction)
                    items.forEach { outputSlot.take(it, transaction) }
                    outputTank.take(fluid, transaction)
                    transaction.commit()
                }
                // TODO: playSound
            }
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

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.centrifuge
}
