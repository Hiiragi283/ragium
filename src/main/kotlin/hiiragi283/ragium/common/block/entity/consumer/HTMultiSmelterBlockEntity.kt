package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.common.recipe.VanillaRecipeTypes
import hiiragi283.ragium.common.recipe.manager.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, Pair<HTVanillaCookingRecipe, Int>>(
        RagiumBlocks.MULTI_SMELTER,
        pos,
        state,
    ) {
    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var catalystSlot: HTItemStackSlot
        private set
    lateinit var outputSlot: HTItemStackSlot
        private set

    override fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // catalyst
        catalystSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
        // output
        outputSlot = singleOutput(builder, listener)
    }

    private val smeltingCache: HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> = HTFinderRecipeCache(
        VanillaRecipeTypes.SMELTING,
    )
    private val blastingCache: HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> = HTFinderRecipeCache(
        VanillaRecipeTypes.BLASTING,
    )
    private val smokingCache: HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> = HTFinderRecipeCache(
        VanillaRecipeTypes.SMOKING,
    )

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): Pair<HTVanillaCookingRecipe, Int>? {
        val cache: HTFinderRecipeCache<SingleRecipeInput, HTVanillaCookingRecipe> = when (catalystSlot.getStack()?.value()) {
            Items.BLAST_FURNACE -> blastingCache
            Items.SMOKER -> smokingCache
            else -> smeltingCache
        }
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

    private fun getMaxParallel(): Int = when (getComponentTier()) {
        HTComponentTier.BASIC -> 2
        HTComponentTier.ADVANCED -> 4
        HTComponentTier.ELITE -> 8
        HTComponentTier.ULTIMATE -> 16
        HTComponentTier.ETERNAL -> inputSlot.getStack()?.maxStackSize() ?: -1
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
                recipe.ingredients[0].test(stack.unwrap()) -> outputCount
                else -> 0
            }
        }, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
