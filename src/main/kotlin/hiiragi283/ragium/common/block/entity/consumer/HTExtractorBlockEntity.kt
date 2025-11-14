package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemWithCatalystBlockEntity(
        RagiumRecipeTypes.EXTRACTING,
        RagiumBlocks.EXTRACTOR,
        pos,
        state,
    ) {
    override fun getTankCapacity(): Int = RagiumConfig.COMMON.extractorTankCapacity.asInt

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiRecipeInput,
        recipe: HTComplexRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
