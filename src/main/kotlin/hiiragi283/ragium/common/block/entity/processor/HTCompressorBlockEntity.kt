package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.common.block.entity.processor.base.HTItemWithCatalystBlockEntity
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemWithCatalystBlockEntity(
        RagiumBlocks.COMPRESSOR,
        pos,
        state,
    ) {
    private val recipeCache: HTRecipeCache<HTRecipeInput, HTItemWithCatalystRecipe> = HTFinderRecipeCache(RagiumRecipeTypes.COMPRESSING)

    override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): HTItemWithCatalystRecipe? =
        recipeCache.getFirstRecipe(input, level)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTItemWithCatalystRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.25f, 0.5f)
    }
}
