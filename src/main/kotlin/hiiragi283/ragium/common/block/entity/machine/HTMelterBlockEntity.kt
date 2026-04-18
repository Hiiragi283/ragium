package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.assembleFluid
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
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
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MELTER, pos, state) {
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity()),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // input
        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        // output
        widgetHolder += HTFluidWidget
            .createTank(
                outputTank,
                HTSlotHelper.getSlotPosX(6),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.OUTPUT)
    }

    //    Serialize    //

    private val cache: HTRecipeCache<SingleRecipeInput, HTMeltingRecipe> =
        HTLookupRecipeCache.forRecipe(RagiumRecipeLookups.MELTING)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<SingleRecipeInput, HTMeltingRecipe>() {
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

        override fun createInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = createInput(inputHandler)

        override fun findRecipe(level: ServerLevel, pos: BlockPos, input: SingleRecipeInput): HTMeltingRecipe? =
            cache.getFirstRecipe(input, level)

        override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<SingleRecipeInput, HTMeltingRecipe>): Boolean =
            recipe.assembleFluid().let(outputHandler::canInsert)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<SingleRecipeInput, HTMeltingRecipe>) {
            // output
            recipe.assembleFluid().let(outputHandler::insert)
            // input
            inputHandler.consume(recipe.recipe.ingredient)

            playSound(SoundEvents.LAVA_POP)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.melter
}
