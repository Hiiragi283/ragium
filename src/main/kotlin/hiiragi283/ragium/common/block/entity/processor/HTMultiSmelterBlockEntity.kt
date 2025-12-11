package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.math.minus
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.upgrade.RagiumUpgradeKeys
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractSmelterBlockEntity
import hiiragi283.ragium.common.recipe.vanilla.HTVanillaCookingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction
import kotlin.math.min
import kotlin.math.pow

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractSmelterBlockEntity(
        RagiumBlocks.MULTI_SMELTER,
        pos,
        state,
    ) {
    override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): HTVanillaCookingRecipe? {
        val cache: HTRecipeCache<SingleRecipeInput, out AbstractCookingRecipe> = getRecipeCache()
        val singleInput = input.toSingleItem() ?: return null
        val baseRecipe: AbstractCookingRecipe = cache.getFirstRecipe(singleInput, level) ?: return null
        val result: ItemStack = baseRecipe.assemble(singleInput, level.registryAccess())
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
            HTItemIngredient(baseRecipe.ingredients[0], inputCount),
        ) { inputIn: HTRecipeInput, provider: HolderLookup.Provider ->
            val singleInputIn: SingleRecipeInput = inputIn.toSingleItem() ?: return@HTVanillaCookingRecipe ItemStack.EMPTY
            baseRecipe.assemble(singleInputIn, provider).copyWithCount(outputCount)
        }
    }

    private fun getMaxParallel(): Int {
        val maxTier: Fraction? = getMaxMultiplier(RagiumUpgradeKeys.BASE_MULTIPLIER)
        return when {
            isCreative() -> inputSlot.getStack()?.maxStackSize() ?: -1
            maxTier != null -> 2.0.pow((maxTier - 1).toDouble()).toInt()
            else -> 1
        }
    }
}
