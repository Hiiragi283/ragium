package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.chance.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [HTChancedItemRecipe]の抽象クラス
 */
abstract class HTBasicChancedItemRecipe<INPUT : RecipeInput> : HTChancedItemRecipe<INPUT> {
    /**
     * 完成品の一覧
     */
    abstract val results: List<HTItemResultWithChance>

    final override fun getResultItems(input: INPUT): List<HTItemResultWithChance> = results

    final override fun assembleItem(input: INPUT, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, results.getOrNull(0)?.base)

    final override fun isIncomplete(): Boolean {
        if (isIncompleteIngredient()) return false
        if (results.isEmpty()) return false
        return results[0].hasNoMatchingStack() || results.all(HTItemResultWithChance::hasNoMatchingStack)
    }

    /**
     * 材料が有効かどうか判定します
     */
    protected abstract fun isIncompleteIngredient(): Boolean
}
