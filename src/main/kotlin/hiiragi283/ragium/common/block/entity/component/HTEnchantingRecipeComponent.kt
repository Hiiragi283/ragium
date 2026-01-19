package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.common.recipe.HTFinderRecipeCache
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.EnchantingTableBlock

abstract class HTEnchantingRecipeComponent<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(
    protected val owner: HTProcessorBlockEntity,
) : HTRecipeComponent<INPUT, RECIPE>() {
    final override fun getMaxProgress(recipe: RECIPE): Int = getTime(recipe).let(owner::modifyTime)

    open fun getTime(recipe: RECIPE): Int = recipe.time

    final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = EnchantingTableBlock.BOOKSHELF_OFFSETS
        .filter { EnchantingTableBlock.isValidBookShelf(level, pos, it) }
        .map(pos::offset)
        .map { level.getBlockState(it).getEnchantPowerBonus(level, it) }
        .sum()
        .toInt()

    abstract class Cached<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(
        private val cache: HTRecipeCache<INPUT, RECIPE>,
        owner: HTProcessorBlockEntity.Energized,
    ) : HTEnchantingRecipeComponent<INPUT, RECIPE>(owner) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            owner: HTProcessorBlockEntity.Energized,
        ) : this(HTFinderRecipeCache(finder), owner)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.getFirstRecipe(input, level)
    }
}
