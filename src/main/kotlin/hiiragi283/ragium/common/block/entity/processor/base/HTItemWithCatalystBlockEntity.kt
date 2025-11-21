package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemWithCatalystBlockEntity(
    finder: HTRecipeFinder<HTMultiRecipeInput, HTComplexRecipe>,
    blockHolder: Holder<Block>,
    pos: BlockPos,
    state: BlockState,
) : HTComplexBlockEntity(finder, blockHolder, pos, state) {
    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var catalystSlot: HTItemStackSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // catalyst
        catalystSlot = singleCatalyst(builder, listener)
        // output
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0.5)),
        )
    }

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput? = HTMultiRecipeInput.create {
        items += inputSlot.getStack()
        items += catalystSlot.getStack()
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiRecipeInput,
        recipe: HTComplexRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // 実際にインプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount.partially1(0), HTStorageAction.EXECUTE)
    }
}
