package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResourcePair
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelAccessor
import org.apache.commons.lang3.math.Fraction

abstract class HTChancedRecipe(
    val result: HTItemResult,
    val extraResults: List<HTChancedItemResult>,
    time: Int,
    exp: Fraction,
) : HTViewProcessingRecipe(time, exp) {
    fun getExtraResultItems(level: LevelAccessor): List<ItemStack> = getExtraResultItems(level.registryAccess(), level.random)

    fun getExtraResultItems(provider: HolderLookup.Provider, random: RandomSource): List<ItemStack> =
        getExtraResultItems(provider, random.nextFloat())

    fun getExtraResultItems(provider: HolderLookup.Provider, random: Float): List<ItemStack> {
        val map: MutableMap<HTItemResourceType, Int> = mutableMapOf()

        for (chanced: HTChancedItemResult in extraResults) {
            val (resource: HTItemResourceType, count: Int) = chanced.getStackOrEmpty(provider, random).toResourcePair() ?: continue
            map.compute(resource) { _, old: Int? -> (old ?: 0) + count }
        }

        return map.map { (resource: HTItemResourceType, count: Int) -> resource.toStack(count) }
    }

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
