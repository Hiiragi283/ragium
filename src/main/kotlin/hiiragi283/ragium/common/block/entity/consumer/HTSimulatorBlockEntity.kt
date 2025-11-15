package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.common.block.entity.consumer.base.HTItemWithCatalystBlockEntity
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTSimulatorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemWithCatalystBlockEntity(
        RagiumRecipeTypes.SIMULATING,
        RagiumBlocks.SIMULATOR,
        pos,
        state,
    ) {
    override fun getOutputTankCapacity(): Int = RagiumConfig.COMMON.simulatorTankCapacity.asInt

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiRecipeInput,
        recipe: HTComplexRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
