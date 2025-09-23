package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [hiiragi283.ragium.api.recipe.HTChancedItemRecipe]を実装した抽象クラス
 */
abstract class HTChancedItemRecipeBase<INPUT : RecipeInput> : HTChancedItemRecipe<INPUT> {
    /**
     * 完成品の一覧
     */
    abstract val results: List<HTChancedItemRecipe.ChancedResult>

    final override fun getResultItems(input: INPUT): List<HTChancedItemRecipe.ChancedResult> = results

    final override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack =
        getItemResult(input, registries, results.getOrNull(0))

    final override fun isIncomplete(): Boolean =
        isIncompleteIngredient() || results.isEmpty() || results.all(HTChancedItemRecipe.ChancedResult::hasNoMatchingStack)

    /**
     * 材料が有効かどうか判定します
     */
    protected abstract fun isIncompleteIngredient(): Boolean
}
