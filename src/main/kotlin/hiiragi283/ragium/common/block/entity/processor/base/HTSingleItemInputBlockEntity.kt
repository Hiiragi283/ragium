package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.recipe.HTAbstractRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleItemInputBlockEntity<RECIPE : HTAbstractRecipe>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.RecipeBased<RECIPE>(blockHolder, pos, state) {
    lateinit var inputSlot: HTBasicItemSlot
        protected set

    final override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items += inputSlot.getStack()
    }

    //    Cached    //

    abstract class Cached<RECIPE : HTAbstractRecipe>(
        private val recipeCache: HTRecipeCache<HTRecipeInput, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTSingleItemInputBlockEntity<RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<HTRecipeInput, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)
    }

    abstract class CachedWithTank<RECIPE : HTAbstractRecipe> : Cached<RECIPE> {
        constructor(
            recipeCache: HTRecipeCache<HTRecipeInput, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : super(recipeCache, blockHolder, pos, state)

        constructor(
            finder: HTRecipeFinder<HTRecipeInput, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : super(finder, blockHolder, pos, state)

        lateinit var inputTank: HTBasicFluidTank
            private set

        final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
            // input
            inputTank = builder.addSlot(HTSlotInfo.INPUT, createTank(listener))
        }

        protected abstract fun createTank(listener: HTContentListener): HTBasicFluidTank
    }
}
