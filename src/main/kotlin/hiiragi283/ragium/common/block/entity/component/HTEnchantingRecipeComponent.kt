package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.EnchantingTableBlock

abstract class HTEnchantingRecipeComponent<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(owner: HTProcessorBlockEntity) :
    HTProcessingRecipeComponent<INPUT, RECIPE>(owner, HTProcessingRecipe<*>::time) {
    final override fun modifyTime(time: Int): Int = time * 20

    final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = EnchantingTableBlock.BOOKSHELF_OFFSETS
        .filter { EnchantingTableBlock.isValidBookShelf(level, pos, it) }
        .map(pos::offset)
        .map { level.getBlockState(it).getEnchantPowerBonus(level, it) }
        .sum()
        .toInt()
}
