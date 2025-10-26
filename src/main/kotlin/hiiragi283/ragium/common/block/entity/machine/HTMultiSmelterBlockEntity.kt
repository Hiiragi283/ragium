package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.createCache
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.insertItem
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTMultiSmelterBlockEntity.MultiSmeltingRecipe>(
        RagiumBlocks.MULTI_SMELTER,
        pos,
        state,
    ) {
    private lateinit var inputSlot: HTItemSlot.Mutable
    private lateinit var catalystSlot: HTItemSlot
    private lateinit var outputSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        // input
        inputSlot = builder.addSlot(
            HTAccessConfig.INPUT_ONLY,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)),
        )
        // catalyst
        catalystSlot = builder.addSlot(
            HTAccessConfig.OUTPUT_ONLY,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
        // output
        outputSlot = builder.addSlot(
            HTAccessConfig.NONE,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1)),
        )
        return builder.build()
    }

    private val smeltingCache: HTRecipeCache<SingleRecipeInput, SmeltingRecipe> = RecipeType.SMELTING.createCache()
    private val blastingCache: HTRecipeCache<SingleRecipeInput, BlastingRecipe> = RecipeType.BLASTING.createCache()
    private val smokingCache: HTRecipeCache<SingleRecipeInput, SmokingRecipe> = RecipeType.SMOKING.createCache()

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = inputSlot.toRecipeInput()

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): MultiSmeltingRecipe? {
        val cache: HTRecipeCache<SingleRecipeInput, out AbstractCookingRecipe> = when (catalystSlot.getStack().value()) {
            Items.BLAST_FURNACE -> blastingCache
            Items.SMOKER -> smokingCache
            else -> smeltingCache
        }
        val baseRecipe: AbstractCookingRecipe = cache.getFirstRecipe(input, level) ?: return null
        val result: ItemStack = baseRecipe.assemble(input, level.registryAccess())
        if (result.isEmpty) return null
        val resultMaxSize: Int = result.maxStackSize

        var inputCount: Int = min(inputSlot.getAmountAsInt(), getMaxParallel())
        val maxParallel: Int = min(inputCount, getMaxParallel())
        var outputCount: Int = result.count * maxParallel
        if (outputCount > resultMaxSize) {
            outputCount = resultMaxSize - (resultMaxSize % maxParallel)
            inputCount = outputCount / maxParallel
        }
        if (inputCount <= 0 || outputCount <= 0) return null
        return MultiSmeltingRecipe(baseRecipe, outputCount)
    }

    private fun getMaxParallel(): Int = when (upgradeHandler.getTier()) {
        HTComponentTier.BASIC -> 2
        HTComponentTier.ADVANCED -> 4
        HTComponentTier.ELITE -> 8
        HTComponentTier.ULTIMATE -> 16
        HTComponentTier.ETERNAL -> inputSlot.getStack().maxStackSize()
        null -> 1
    }

    override fun getRecipeTime(recipe: MultiSmeltingRecipe): Int = recipe.recipe.cookingTime

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: MultiSmeltingRecipe): Boolean =
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: MultiSmeltingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insertItem(recipe.assemble(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }

    @JvmRecord
    data class MultiSmeltingRecipe(val recipe: AbstractCookingRecipe, val count: Int) : HTItemIngredient.CountGetter {
        fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
            recipe.assemble(input, registries).copyWithCount(count)

        override fun getRequiredCount(stack: ItemStack): Int = when {
            recipe.ingredients[0].test(stack) -> count
            else -> 0
        }
    }
}
