package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.HTTypedProgressHandler
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.support.recipe.handler.HTFluidInputHandler
import hiiragi283.core.support.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.support.storage.fluid.HTBasicFluidTank
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.fluid.HTVariableFluidTank
import hiiragi283.ragium.support.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidDuplicatorBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.FLUID_DUPLICATOR.get(), pos, state) {
    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var matterTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(
                listener,
                getTankCapacity(),
                filter = { !it.isOf(RagiumTags.Fluids.LIQUID_MATTER) },
            ),
        )
        matterTank = builder.addSlot(
            HTSlotInfo.EXTRA_INPUT,
            HTVariableFluidTank.input(
                listener,
                getTankCapacity(),
                filter = { it.isOf(RagiumTags.Fluids.LIQUID_MATTER) },
            ),
        )
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity()))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(2))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(5.25))
        // tanks
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        widgetHolder += HTFluidWidget.Tank(
            matterTank,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.EXTRA_INPUT,
            false,
        )
        widgetHolder.track(matterTank)

        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : HTTypedProgressHandler<FluidStack>() {
        private val inputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val matterHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(matterTank) }
        private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

        override fun findRecipe(level: ServerLevel, pos: BlockPos): FluidStack? {
            val amount: Int = intArrayOf(
                inputHandler.getAmount(),
                matterHandler.getAmount(),
                outputTank.getNeeded(),
            ).minOrNull() ?: return null
            return inputHandler.getFluidStack().copyWithAmount(amount).takeUnless(FluidStack::isEmpty)
        }

        override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: FluidStack): Boolean = outputHandler.canInsert(recipe)

        override fun getMaxProgress(recipe: FluidStack): Int = updateAndGetProgress(recipe.amount)

        override fun getProgress(level: ServerLevel, pos: BlockPos): Int = handler.consume()

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: FluidStack) {
            // output
            outputHandler.insert(recipe)
            // inputs
            inputHandler.consume(recipe.amount)
            matterHandler.consume(recipe.amount)
            // sound
            playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE)
        }
    }

    override fun createHandler(): HTProgressHandler = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.fluidDuplicator
}
