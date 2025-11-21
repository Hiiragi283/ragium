package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.single.HTExpRequiredRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTEnchantProcessorBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTEnchanterBlockEntity(pos: BlockPos, state: BlockState) :
    HTEnchantProcessorBlockEntity.Cached<SingleRecipeInput, HTExpRequiredRecipe>(
        RagiumRecipeTypes.ENCHANTING,
        RagiumBlocks.ENCHANTER,
        pos,
        state,
    ) {
    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // output
        outputSlot = singleOutput(builder, listener)
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = inputSlot.toRecipeInput()

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTExpRequiredRecipe): Boolean {
        val bool1: Boolean = outputSlot.insert(
            recipe.assembleItem(input, level.registryAccess()),
            HTStorageAction.SIMULATE,
            HTStorageAccess.INTERNAL,
        ) == null
        val required: Int = recipe.getRequiredExpFluid()
        val bool2: Boolean =
            inputTank.extract(required, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)?.amount() == required
        return bool1 && bool2
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTExpRequiredRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        HTStackSlotHelper.shrinkStack(inputTank, { recipe.getRequiredExpFluid() }, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1f, 1f)
    }
}
