package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.multi.HTItemToExtraItemRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractCrusherBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractCrusherBlockEntity(
        RagiumBlocks.CRUSHER,
        pos,
        state,
    ) {
    lateinit var outputSlot: HTBasicItemSlot
        private set
    lateinit var extraSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // outputs
        outputSlot = upperOutput(builder, listener)
        extraSlot = extraOutput(builder, listener)
    }

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0 && extraSlot.getNeeded() > 0

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToExtraItemRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        val bool1: Boolean = HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)
        if (hasUpgrade(RagiumItems.PRIMARY_ONLY_UPGRADE)) {
            return bool1
        }
        val bool2: Boolean = HTStackSlotHelper.canInsertStack(extraSlot, input, level, recipe::assembleExtraItem)
        return bool1 && bool2
    }

    override fun completeOutput(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToExtraItemRecipe) {
        // 実際にアウトプットに搬出する
        val access: RegistryAccess = level.registryAccess()
        outputSlot.insert(recipe.assembleItem(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        if (!hasUpgrade(RagiumItems.PRIMARY_ONLY_UPGRADE)) {
            extraSlot.insert(recipe.assembleExtraItem(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }
}
