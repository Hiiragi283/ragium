package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.consumer.base.HTFluidToChancedItemOutputBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTWasherBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidToChancedItemOutputBlockEntity.Cached<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe>(
        RagiumRecipeTypes.WASHING,
        RagiumBlocks.WASHER,
        pos,
        state,
    ) {
    override fun createTank(listener: HTContentListener): HTVariableFluidStackTank =
        HTVariableFluidStackTank.input(listener, RagiumConfig.COMMON.washerTankCapacity)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inputSlot, inputTank)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTItemWithFluidToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        HTStackSlotHelper.shrinkStack(inputTank, recipe::getRequiredAmount, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1f, 0.25f)
    }
}
