package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.HTTypedProgressHandler
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.support.recipe.handler.HTFluidInputHandler
import hiiragi283.core.support.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.support.storage.fluid.HTBasicFluidTank
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.support.storage.fluid.HTVariableFluidTank
import hiiragi283.ragium.support.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTMassFabricatorBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MASS_FABRICATOR.get(), pos, state) {
    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(
                listener,
                getTankCapacity(),
                filter = { resource: HTFluidResourceType -> resource.isOf(HiiragiCoreTags.Fluids.ELDRITCH) },
            ),
        )

        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity()))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(2))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(3.75))
        // tanks
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)

        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.massFabricator

    //    Processing    //

    private inner class ProgressHandlerImpl : HTTypedProgressHandler<FluidStack>() {
        private val inputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

        override fun findRecipe(level: ServerLevel, pos: BlockPos): FluidStack? {
            val amount: Int = minOf(inputTank.getAmount(), outputTank.getNeeded())
            return when {
                amount > 0 -> RagiumFluids.RAGI_MATTER.toStack(amount)
                else -> null
            }
        }

        override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: FluidStack): Boolean = outputHandler.canInsert(recipe)

        override fun getMaxProgress(recipe: FluidStack): Int = updateAndGetProgress(recipe.amount / 5)

        override fun getProgress(level: ServerLevel, pos: BlockPos): Int = handler.consume()

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: FluidStack) {
            // output
            outputHandler.insert(recipe)
            // input
            inputHandler.consume(FluidType.BUCKET_VOLUME)
            // sound
            playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE)
        }
    }

    override fun createHandler(): HTProgressHandler = ProgressHandlerImpl()
}
