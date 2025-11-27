package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.common.block.entity.processor.HTEnergizedProcessorBlockEntity
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleItemInputBlockEntity<RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTEnergizedProcessorBlockEntity<SingleRecipeInput, RECIPE>(blockHolder, pos, state) {
    lateinit var inputSlot: HTItemStackSlot
        protected set

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = inputSlot.toRecipeInput()

    //    Cached    //

    abstract class Cached<RECIPE : Recipe<SingleRecipeInput>>(
        private val recipeCache: HTRecipeCache<SingleRecipeInput, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTSingleItemInputBlockEntity<RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<SingleRecipeInput, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): RECIPE? =
            recipeCache.getFirstRecipe(input, level)
    }
}
