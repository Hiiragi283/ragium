package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTLocationToRecipe
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.input.HTLocationRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTLocationToFluidBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {

    fun createRecipeHandler(
        lookup: HTRecipeLookup<HTLocationToRecipe<FluidStack>>,
        sound: HTSoundInstance
    ): HTRecipeHandler<*, *, *> =
        object : CachedHandler<HTLocationRecipeInput, FluidStack, HTLocationToRecipe<FluidStack>>(lookup) {
            private val outputTank: HTOutputSlot<FluidStack> by lazy {
                HTOutputSlotHelper.forFluid(this@HTLocationToFluidBlockEntity.outputTank)
            }

            override fun createProgressor(): Progressor<HTLocationRecipeInput, HTLocationToRecipe<FluidStack>> =
                EnergizedProgressor()

            override fun createInput(level: ServerLevel): HTLocationRecipeInput = HTLocationRecipeInput(level, blockPos)

            override fun canComplete(
                recipe: HTLocationToRecipe<FluidStack>,
                input: HTLocationRecipeInput,
                output: FluidStack
            ): Boolean = useTransaction { outputTank.take(output, it) == HTOutputSlot.TakeResult.FULL }

            override fun onComplete(
                recipe: HTLocationToRecipe<FluidStack>,
                input: HTLocationRecipeInput,
                output: FluidStack
            ) {
                useTransaction {
                    outputTank.take(output, it)
                    it.commit()
                }
                playSound(sound)
            }
        }

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
            false
        )
        widgetHolder.track(outputTank)
    }
}
