package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTFreezerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.FREEZER, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity()),
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
        // output
        widgetHolder += HTItemSlotWidget.container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Serialize    //

    private val cache: HTRecipeCache<HTItemAndFluidRecipeInput, HTFreezingRecipe> =
        HTLookupRecipeCache.forRecipe(RagiumRecipeLookups.FREEZING)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTItemAndFluidRecipeInput, HTFreezingRecipe>() {
        private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }

        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun createInput(level: ServerLevel, pos: BlockPos): HTItemAndFluidRecipeInput? =
            createInput(itemInputHandler, fluidInputHandler)

        override fun findRecipe(level: ServerLevel, pos: BlockPos, input: HTItemAndFluidRecipeInput): HTFreezingRecipe? =
            cache.getFirstRecipe(input, level)

        override fun canComplete(
            level: ServerLevel,
            pos: BlockPos,
            recipe: HTHandledRecipe<HTItemAndFluidRecipeInput, HTFreezingRecipe>,
        ): Boolean = recipe.assemble(true).let(outputHandler::canInsert)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<HTItemAndFluidRecipeInput, HTFreezingRecipe>) {
            // output
            recipe.assemble(false).let(outputHandler::insert)
            // input
            val recipe: HTFreezingRecipe = recipe.recipe
            fluidInputHandler.consume(recipe.ingredient)
            itemInputHandler.consume(1)

            playSound(SoundEvents.GLASS_HIT)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.freezer
}
