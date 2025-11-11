package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTSimulatorBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTMultiRecipeInput, HTItemWithCatalystToItemRecipe>(
        RagiumRecipeTypes.SIMULATING,
        RagiumBlocks.SIMULATOR,
        pos,
        state,
    ) {
    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var catalystSlot: HTItemStackSlot
        private set
    lateinit var outputSlot: HTItemStackSlot
        private set

    override fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // catalyst
        catalystSlot = singleCatalyst(builder, listener)
        // output
        outputSlot = singleOutput(builder, listener)
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput = HTMultiRecipeInput.fromSlots(inputSlot, catalystSlot)

    override fun canProgressRecipe(level: ServerLevel, input: HTMultiRecipeInput, recipe: HTItemWithCatalystToItemRecipe): Boolean =
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiRecipeInput,
        recipe: HTItemWithCatalystToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe.ingredient, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
