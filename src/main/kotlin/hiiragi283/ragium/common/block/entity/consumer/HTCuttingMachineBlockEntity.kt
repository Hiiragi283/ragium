package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.common.block.entity.consumer.base.HTChancedItemOutputBlockEntity
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTChancedItemOutputBlockEntity.Cached<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CUTTING,
        RagiumBlocks.CUTTING_MACHINE,
        pos,
        state,
    ) {
    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        inputSlot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1f, 1f)
    }
}
