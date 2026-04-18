package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.assembleFluid
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemOrFluidBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity()),
        )
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity()),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // inputs
        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTFluidWidget
            .createTank(
                inputTank,
                HTSlotHelper.getSlotPosX(1),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.EXTRA_INPUT)
        // outputs
        widgetHolder += HTItemSlotWidget.container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder += HTFluidWidget
            .createTank(
                outputTank,
                HTSlotHelper.getSlotPosX(7.5),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.EXTRA_OUTPUT)
    }

    //    Serialize    //

    private val cache: HTRecipeCache<HTItemAndFluidRecipeInput, out HTItemOrFluidRecipe> =
        HTLookupRecipeCache.forRecipe(getLookup())

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe>() {
        private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }

        private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }
        private val itemOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun createInput(level: ServerLevel, pos: BlockPos): HTItemAndFluidRecipeInput? =
            createInput(itemInputHandler, fluidInputHandler)

        override fun findRecipe(level: ServerLevel, pos: BlockPos, input: HTItemAndFluidRecipeInput): HTItemOrFluidRecipe? =
            cache.getFirstRecipe(input, level)

        override fun canComplete(
            level: ServerLevel,
            pos: BlockPos,
            recipe: HTHandledRecipe<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe>,
        ): Boolean {
            val bool1: Boolean = itemOutputHandler.canInsert(recipe.assemble(true))
            val bool2: Boolean = fluidOutputHandler.canInsert(recipe.assembleFluid())
            return bool1 && bool2
        }

        override fun onComplete(
            level: ServerLevel,
            pos: BlockPos,
            recipe: HTHandledRecipe<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe>,
        ) {
            // output
            itemOutputHandler.insert(recipe.assemble(false))
            fluidOutputHandler.insert(recipe.assembleFluid())
            // input
            val (itemAmount: Int?, fluidAmount: Int?) = recipe.map(HTItemOrFluidRecipe::getRequiredAmount).toPair()
            fluidInputHandler.consume(fluidAmount ?: 0)
            itemInputHandler.consume(itemAmount ?: 0)

            playSound()
        }
    }

    final override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    protected abstract fun getLookup(): HTRecipeLookup<HTItemAndFluidRecipeInput, out HTItemOrFluidRecipe>

    protected abstract fun playSound()
}
