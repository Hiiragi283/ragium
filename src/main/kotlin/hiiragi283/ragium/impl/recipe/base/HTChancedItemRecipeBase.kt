package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [hiiragi283.ragium.api.recipe.HTChancedItemRecipe]を実装した抽象クラス
 */
abstract class HTChancedItemRecipeBase<INPUT : RecipeInput> : HTChancedItemRecipe<INPUT> {
    /**
     * 完成品の一覧
     */
    abstract val results: List<HTChancedItemResult>

    final override fun getResultItems(input: INPUT): List<HTChancedItemResult> = results

    final override fun assembleItem(input: INPUT, registries: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, registries, results.getOrNull(0)?.base)

    final override fun isIncomplete(): Boolean =
        isIncompleteIngredient() || results.isEmpty() || results.all(HTChancedItemResult::hasNoMatchingStack)

    /**
     * 材料が有効かどうか判定します
     */
    protected abstract fun isIncompleteIngredient(): Boolean
}
