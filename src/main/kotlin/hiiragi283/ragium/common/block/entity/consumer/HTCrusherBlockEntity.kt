package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidToChancedItemOutputBlockEntity.Cached<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING,
        RagiumBlocks.CRUSHER,
        pos,
        state,
    ) {
    override fun createTank(listener: HTContentListener): HTVariableFluidStackTank = HTVariableFluidStackTank.input(
        listener,
        RagiumConfig.COMMON.crusherTankCapacity,
        canInsert = RagiumFluidContents.LUBRICANT::isOf,
    )

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    override fun getRecipeTime(recipe: HTItemToChancedItemRecipe): Int =
        when (inputTank.extract(10, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)?.amount()) {
            10 -> 18 * 10
            else -> super.getRecipeTime(recipe)
        }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        // 潤滑油があれば減らす
        inputTank.extract(10, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1f, 0.25f)
    }
}
