package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTBrewingRecipeData
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractCombinerBlockEntity
import hiiragi283.ragium.common.recipe.HTBrewingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.streams.asSequence

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) : HTAbstractCombinerBlockEntity(FINDER, RagiumBlocks.BREWERY, pos, state) {
    companion object {
        @JvmStatic
        private val FAKE_ID: ResourceLocation = RagiumAPI.id("/brewing/fake")

        @JvmStatic
        private val FINDER = HTRecipeFinder { _, input: HTRecipeInput, level: Level, lastRecipe: RecipeHolder<HTCombineRecipe>? ->
            if (lastRecipe != null && lastRecipe.value().matches(input, level)) {
                return@HTRecipeFinder lastRecipe
            }
            level
                .registryAccess()
                .lookupOrThrow(RagiumAPI.BREWING_RECIPE_KEY)
                .listElements()
                .asSequence()
                .forEach { holder: Holder.Reference<HTBrewingRecipeData> ->
                    val recipeData: HTBrewingRecipeData = holder.value()

                    val base: HTBrewingRecipe = HTBrewingRecipe.createBaseRecipe(recipeData)
                    if (base.matches(input, level)) {
                        return@HTRecipeFinder RecipeHolder(FAKE_ID, base)
                    }
                    HTBrewingRecipe.createLongRecipe(recipeData)?.let { long: HTBrewingRecipe ->
                        if (long.matches(input, level)) {
                            return@HTRecipeFinder RecipeHolder(FAKE_ID, long)
                        }
                    }
                    HTBrewingRecipe.createStrongRecipe(recipeData)?.let { strong: HTBrewingRecipe ->
                        if (strong.matches(input, level)) {
                            return@HTRecipeFinder RecipeHolder(FAKE_ID, strong)
                        }
                    }
                }
            null
        }
    }

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
