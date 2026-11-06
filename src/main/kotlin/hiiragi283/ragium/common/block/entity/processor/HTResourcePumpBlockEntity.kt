package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTLocationToFluidRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
import hiiragi283.lib.recipe.handler.output.HTOutputHandlerHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTLocationToBlockEntity
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTResourcePumpBlockEntity(pos: BlockPos, state: BlockState) :
    HTLocationToBlockEntity<FluidStack, HTLocationToFluidRecipe>(RagiumBlockEntityTypes.RESOURCE_PUMP.get(), pos, state) {

    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // output
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }

    //    HTLocationToBlockEntity    //

    private val outputHandler: HTOutputHandler<FluidStack> = HTOutputHandlerHelper.forFluid(outputTank)

    override fun getLookup(): HTRecipeLookup<HTLocationToFluidRecipe> = RagiumRecipeLookups.RESOURCE_PUMPING

    override fun canComplete(recipe: HTLocationToFluidRecipe, output: FluidStack): Boolean =
        useTransaction { outputHandler.take(output, it) == HTOutputHandler.TakeResult.FULL }

    override fun onComplete(recipe: HTLocationToFluidRecipe, level: ServerLevel, pos: BlockPos, output: FluidStack) {
        useTransaction {
            outputHandler.take(output, it)
            it.commit()
        }

        playSound(SoundEvents.BUCKET_FILL)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.resourcePump
}
