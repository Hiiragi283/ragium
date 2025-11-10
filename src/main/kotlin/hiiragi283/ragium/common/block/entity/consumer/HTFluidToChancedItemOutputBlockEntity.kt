package hiiragi283.ragium.common.block.entity.consumer

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeFinder
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTFluidToChancedItemOutputBlockEntity<INPUT : RecipeInput, RECIPE : HTChancedItemRecipe<INPUT>>(
    blockHolder: Holder<Block>,
    pos: BlockPos,
    state: BlockState,
) : HTChancedItemOutputBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
    lateinit var inputTank: HTFluidStackTank
        private set

    final override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        // input
        inputTank = builder.addSlot(HTSlotInfo.INPUT, createTank(listener))
        return builder.build()
    }

    protected abstract fun createTank(listener: HTContentListener): HTFluidStackTank

    //    Cached    //

    abstract class Cached<INPUT : RecipeInput, RECIPE : HTChancedItemRecipe<INPUT>>(
        private val cache: HTRecipeCache<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTFluidToChancedItemOutputBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(finder.createCache(), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
