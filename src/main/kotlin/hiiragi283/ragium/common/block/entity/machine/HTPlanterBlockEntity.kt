package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.fluid.HTBasicFluidTank
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.recipe.base.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.cache.completed.HTDoubleToMultiItemCompletedRecipe
import hiiragi283.ragium.common.block.entity.machine.base.HTMultiItemBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.fluid.HTVariableFluidTank
import hiiragi283.ragium.support.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPlanterBlockEntity(pos: BlockPos, state: BlockState) : HTMultiItemBlockEntity(RagiumBlockEntityTypes.PLANTER.get(), pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank =
            builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTVariableFluidTank.input(listener, getTankCapacity()))
    }

    private lateinit var plantSlot: HTBasicItemSlot
    private lateinit var soilSlot: HTBasicItemSlot

    override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        plantSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener, limit = 1))
        soilSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.input(listener, limit = 1))
    }

    override fun getOutputSlotSize(): Int = 4

    //    Processing    //

    private inner class ProgressHandlerImpl : SimpleProgressHandler<HTPlantingRecipe, HTDoubleToMultiItemCompletedRecipe.Planting>(SoundEvents.GROWING_PLANT_CROP) {
        private val cache: HTRecipeCaches.DoubleItem<HTPlantingRecipe> = HTRecipeCaches.DoubleItem(RagiumRecipeLookups.PLANTING)

        private val plantInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(plantSlot) }
        private val soilInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(soilSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(outputSlots) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTPlantingRecipe? = cache.findFirstRecipe(plantInputHandler.getStack(), soilInputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTPlantingRecipe): HTDoubleToMultiItemCompletedRecipe.Planting = HTDoubleToMultiItemCompletedRecipe.Planting(recipe, plantInputHandler, soilInputHandler, outputHandler)
    }

    override fun createHandler(): HTProgressHandler = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.planter
}
