package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidDuplicatorBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.FLUID_DUPLICATOR, pos, state) {
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
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(2))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(5.25))
        // tanks
        widgetHolder += HTFluidWidget
            .createTank(
                inputTank,
                HTSlotHelper.getSlotPosX(1),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.INPUT)
        widgetHolder += HTFluidWidget
            .createTank(
                matterTank,
                HTSlotHelper.getSlotPosX(4),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.EXTRA_INPUT)

        widgetHolder += HTFluidWidget
            .createTank(
                outputTank,
                HTSlotHelper.getSlotPosX(7),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.OUTPUT)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : HTProgressHandler<FluidStack>() {
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

        override fun getProgress(level: ServerLevel, pos: BlockPos): Int = battery.consume()

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

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.fluidDuplicator
}
