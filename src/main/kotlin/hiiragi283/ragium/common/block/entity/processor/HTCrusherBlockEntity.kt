package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractCrusherBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractCrusherBlockEntity(
        RagiumBlocks.CRUSHER,
        pos,
        state,
    ) {
    lateinit var outputSlots: List<HTItemStackSlot>
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // outputs
        outputSlots = multiOutputs(builder, listener)
    }

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean =
        outputSlots.any { slot: HTItemStackSlot -> slot.getNeeded() > 0 }

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        for (stackIn: ImmutableItemStack in recipe.getPreviewItems(input, level.registryAccess())) {
            if (HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.SIMULATE) != null) {
                return false
            }
        }
        return true
    }

    override fun completeOutput(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe) {
        // 実際にアウトプットに搬出する
        for (result: HTItemResultWithChance in recipe.getResultItems(input)) {
            val stackIn: ImmutableItemStack = result.getStackOrNull(level.registryAccess(), level.random, this) ?: continue
            HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.EXECUTE)
        }
    }
}
