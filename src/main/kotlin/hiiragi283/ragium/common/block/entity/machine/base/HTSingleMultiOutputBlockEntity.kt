package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleMultiOutputBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMultiOutputBlockEntity<SingleRecipeInput, HTSingleMultiOutputRecipe>(type, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot

    final override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    //    Processing    //

    private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }

    final override fun createInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = createInput(inputHandler)

    final override fun onComplete(
        level: ServerLevel,
        pos: BlockPos,
        recipe: HTHandledRecipe<SingleRecipeInput, out HTSingleMultiOutputRecipe>,
    ) {
        inputHandler.consume(recipe.map(HTSingleMultiOutputRecipe::getRequiredAmount))
        playSound()
    }

    protected abstract fun playSound()
}
