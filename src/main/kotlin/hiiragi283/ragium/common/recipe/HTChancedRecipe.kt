package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import org.apache.commons.lang3.math.Fraction

abstract class HTChancedRecipe(
    val ingredient: HTItemIngredient,
    val result: HTItemResult,
    val extraResults: List<HTChancedItemResult>,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe(time, exp) {
    fun getResultItems(level: LevelAccessor): List<ItemStack> = getResultItems(level.registryAccess(), level.random)

    fun getResultItems(provider: HolderLookup.Provider, random: RandomSource): List<ItemStack> {
        val map: MutableMap<HTItemResourceType, Int> = mutableMapOf()

        val stack: ItemStack = result.getStackOrEmpty(provider)
        stack.toResource()?.let { map[it] = stack.count }

        for (chanced: HTChancedItemResult in extraResults) {
            val stackIn: ItemStack = chanced.getStackOrEmpty(provider, random)
            val resourceIn: HTItemResourceType = stackIn.toResource() ?: continue
            map.compute(resourceIn) { _, old: Int? -> (old ?: 0) + stackIn.count }
        }

        return map.map { (resource: HTItemResourceType, count: Int) -> resource.toStack(count) }
    }

    final override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testItem(0, ingredient)

    final override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
