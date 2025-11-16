package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.chance.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.manager.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTChancedItemOutputBlockEntity<INPUT : RecipeInput, RECIPE : HTChancedItemRecipe<INPUT>>(
    blockHolder: Holder<Block>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var outputSlots: List<HTItemStackSlot>
        private set

    final override fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // outputs
        outputSlots = multiOutputs(builder, listener)
    }

    override fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean {
        // アウトプットに搬出できるか判定する
        for (stackIn: ImmutableItemStack in recipe.getPreviewItems(input, level.registryAccess())) {
            if (HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.SIMULATE) != null) {
                return false
            }
        }
        return true
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    ) {
        // 実際にアウトプットに搬出する
        for (result: HTItemResultWithChance in recipe.getResultItems(input)) {
            val stackIn: ImmutableItemStack = result.getStackOrNull(level.registryAccess(), level.random) ?: continue
            HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.EXECUTE)
        }
    }

    //    Cached    //

    abstract class Cached<INPUT : RecipeInput, RECIPE : HTChancedItemRecipe<INPUT>>(
        private val cache: HTRecipeCache<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTChancedItemOutputBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
