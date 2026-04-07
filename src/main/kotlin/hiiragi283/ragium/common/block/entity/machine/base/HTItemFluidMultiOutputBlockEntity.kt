package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.ragium.api.recipe.base.HTItemFluidMultiOutputRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemFluidMultiOutputBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMultiOutputBlockEntity<HTItemAndFluidRecipeInput, HTItemFluidMultiOutputRecipe>(type, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    final override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity()),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot

    final override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    //    Processing    //

    private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }

    final override fun createInput(level: ServerLevel, pos: BlockPos): HTItemAndFluidRecipeInput? =
        createInput(itemInputHandler, fluidInputHandler)

    override fun onComplete(
        level: ServerLevel,
        pos: BlockPos,
        recipe: HTHandledRecipe<HTItemAndFluidRecipeInput, out HTItemFluidMultiOutputRecipe>,
    ) {
        itemInputHandler.consume(recipe.map(HTItemFluidMultiOutputRecipe::getRequiredCount))
        fluidInputHandler.consume(recipe.map(HTItemFluidMultiOutputRecipe::getRequiredAmount))
        playSound()
    }

    protected abstract fun playSound()
}
