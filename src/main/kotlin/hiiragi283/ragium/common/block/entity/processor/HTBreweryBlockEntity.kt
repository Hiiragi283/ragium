package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractCombinerBlockEntity
import hiiragi283.ragium.common.recipe.HTBrewingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractCombinerBlockEntity(RagiumRecipeTypes.BREWING, RagiumBlocks.BREWERY, pos, state) {
    override fun filterFluid(stack: ImmutableFluidStack): Boolean = HTBrewingRecipe.FLUID_INGREDIENT.testOnlyType(stack)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTCombineRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1f, 1f)
    }
}
