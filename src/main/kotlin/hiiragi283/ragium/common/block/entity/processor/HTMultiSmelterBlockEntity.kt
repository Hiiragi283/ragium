package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractSmelterBlockEntity
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractSmelterBlockEntity(
        RagiumBlocks.MULTI_SMELTER,
        pos,
        state,
    ) {
    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTVanillaCookingRecipe? {
        val cache: HTRecipeCache<SingleRecipeInput, out AbstractCookingRecipe> = getRecipeCache()
        val baseRecipe: AbstractCookingRecipe = cache.getFirstRecipe(input, level) ?: return null
        val result: ItemStack = baseRecipe.assemble(input, level.registryAccess())
        if (result.isEmpty) return null
        val resultMaxSize: Int = result.maxStackSize

        var inputCount: Int = min(inputSlot.getAmount(), getMaxParallel())
        val maxParallel: Int = min(inputCount, getMaxParallel())
        var outputCount: Int = result.count * maxParallel
        if (outputCount > resultMaxSize) {
            outputCount = resultMaxSize - (resultMaxSize % maxParallel)
            inputCount = outputCount / maxParallel
        }
        if (inputCount <= 0 || outputCount <= 0) return null
        return HTVanillaCookingRecipe(
            baseRecipe,
            HTItemIngredient.convert(baseRecipe.ingredients[0], inputCount),
            baseRecipe::assemble.andThen { it.copyWithCount(outputCount) },
        )
    }

    private fun getMaxParallel(): Int = when (getMaxMachineTier()) {
        HTBaseTier.BASIC -> 2
        HTBaseTier.ADVANCED -> 4
        HTBaseTier.ELITE -> 8
        HTBaseTier.ULTIMATE -> 16
        HTBaseTier.CREATIVE -> inputSlot.getStack()?.maxStackSize() ?: -1
        null -> 1
    }
}
