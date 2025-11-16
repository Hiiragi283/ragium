package hiiragi283.ragium.common.block.entity.arcane

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.manager.HTFinderRecipeCache
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.state.BlockState

/**
 * 周囲からエンチャントパワーを吸い取って稼働する設備に使用される[HTMachineBlockEntity]の拡張クラス
 */
abstract class HTArcaneBlockEntity<INPUT : Any, RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
    //    Ticking    //

    final override fun gatherEnergy(level: ServerLevel, pos: BlockPos): Int = EnchantingTableBlock.BOOKSHELF_OFFSETS
        .filter { posIn: BlockPos -> EnchantingTableBlock.isValidBookShelf(level, pos, posIn) }
        .map { posIn: BlockPos ->
            val posTo: BlockPos = pos.offset(posIn)
            level.getBlockState(posTo).getEnchantPowerBonus(level, posTo)
        }.sum()
        .toInt()

    //    Cached    //

    /**
     * レシピのキャッシュを保持する[HTArcaneBlockEntity]の拡張クラス
     */
    abstract class Cached<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
        private val recipeCache: HTRecipeCache<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTArcaneBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)
    }
}
