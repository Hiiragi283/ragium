package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.recipe.HTAbstractRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleItemOutputBlockEntity<RECIPE : HTAbstractRecipe>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.RecipeBased<RECIPE>(blockHolder, pos, state) {
    lateinit var outputSlot: HTBasicItemSlot
        protected set

    final override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: RECIPE): Boolean =
        HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: RECIPE,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
    }

    //    Cached    //

    abstract class Cached<RECIPE : HTAbstractRecipe>(
        private val recipeCache: HTRecipeCache<HTRecipeInput, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTSingleItemOutputBlockEntity<RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<HTRecipeInput, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)
    }
}
