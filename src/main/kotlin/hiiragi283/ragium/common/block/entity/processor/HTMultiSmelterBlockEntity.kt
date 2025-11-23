package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractSmelterBlockEntity
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractSmelterBlockEntity<Pair<HTVanillaCookingRecipe, Int>>(
        RagiumBlocks.MULTI_SMELTER,
        pos,
        state,
    ) {
    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): Pair<HTVanillaCookingRecipe, Int>? {
        val cache: HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> = getRecipeCache()
        val baseRecipe: HTVanillaCookingRecipe = cache.getFirstRecipe(input, level) ?: return null
        val result: ImmutableItemStack = baseRecipe.assembleItem(input, level.registryAccess()) ?: return null
        val resultMaxSize: Int = result.maxStackSize()

        var inputCount: Int = min(inputSlot.getAmount(), getMaxParallel())
        val maxParallel: Int = min(inputCount, getMaxParallel())
        var outputCount: Int = result.amount() * maxParallel
        if (outputCount > resultMaxSize) {
            outputCount = resultMaxSize - (resultMaxSize % maxParallel)
            inputCount = outputCount / maxParallel
        }
        if (inputCount <= 0 || outputCount <= 0) return null
        return baseRecipe to outputCount
    }

    private fun getMaxParallel(): Int = when (getMaxMachineTier()) {
        HTBaseTier.BASIC -> 2
        HTBaseTier.ADVANCED -> 4
        HTBaseTier.ELITE -> 8
        HTBaseTier.ULTIMATE -> 16
        HTBaseTier.CREATIVE -> inputSlot.getStack()?.maxStackSize() ?: -1
        null -> 1
    }

    override fun getRecipeTime(recipe: Pair<HTVanillaCookingRecipe, Int>): Int = recipe.first.cookingTime

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: Pair<HTVanillaCookingRecipe, Int>): Boolean {
        val (recipe: HTVanillaCookingRecipe, outputCount: Int) = recipe
        val result: ImmutableItemStack? = recipe.assembleItem(input, level.registryAccess())?.copyWithAmount(outputCount)
        return outputSlot.insert(result, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: Pair<HTVanillaCookingRecipe, Int>,
    ) {
        // 実際にアウトプットに搬出する
        val (recipe: HTVanillaCookingRecipe, outputCount: Int) = recipe
        val result: ImmutableItemStack? = recipe.assembleItem(input, level.registryAccess())?.copyWithAmount(outputCount)
        outputSlot.insert(result, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, { stack: ImmutableItemStack ->
            when {
                recipe.ingredient.test(stack.unwrap()) -> outputCount
                else -> 0
            }
        }, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
