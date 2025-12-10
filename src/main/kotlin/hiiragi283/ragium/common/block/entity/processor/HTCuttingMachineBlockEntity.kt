package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.extra.HTSingleExtraItemRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTSingleExtraItemRecipe>(
        RagiumRecipeTypes.CUTTING,
        RagiumBlocks.CUTTING_MACHINE,
        pos,
        state,
    ) {
    lateinit var inputSlot: HTBasicItemSlot
        private set
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

    override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items += inputSlot.getStack()
    }

    override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTSingleExtraItemRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        val bool1: Boolean = HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)
        if (hasUpgrade(RagiumItems.PRIMARY_ONLY_UPGRADE)) {
            return bool1
        }
        val bool2: Boolean = HTStackSlotHelper.canInsertStack(extraSlot, input, level, recipe::assembleExtraItem)
        return bool1 && bool2
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTSingleExtraItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        val access: RegistryAccess = level.registryAccess()
        outputSlot.insert(recipe.assembleItem(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        if (!hasUpgrade(RagiumItems.PRIMARY_ONLY_UPGRADE)) {
            extraSlot.insert(recipe.assembleExtraItem(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
        // インプットを減らす
        inputSlot.extract(recipe.getRequiredCount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }
}
