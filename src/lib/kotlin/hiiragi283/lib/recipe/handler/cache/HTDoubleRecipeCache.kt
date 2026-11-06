package hiiragi283.lib.recipe.handler.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.util.right

/**
 * 2つの入力に対する[HTBaseRecipeCache]の拡張クラスです。
 * @param INPUT_A 1番目のレシピの入力となるクラス
 * @param INPUT_B 2番目のレシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
abstract class HTDoubleRecipeCache<INPUT_A : Any, INPUT_B : Any, RECIPE : Any>(lookup: HTRecipeLookup<RECIPE>) :
    HTBaseRecipeCache<RECIPE>(lookup) {
    /**
     * レシピを取得します。
     * @param first レシピの1番目の入力
     * @param second レシピの2番目の入力
     * @param context レシピ取得時のコンテキスト
     * @return [first]と[second]に一致する最初のレシピ
     */
    fun findFirstRecipe(first: INPUT_A, second: INPUT_B, context: HTRecipeLookup.Context): RECIPE? = findFirstHolder(first, second, context)?.recipe

    /**
     * [HTRecipeHolder]を取得します。
     * @param first レシピの1番目の入力
     * @param second レシピの2番目の入力
     * @param context レシピ取得時のコンテキスト
     * @return [first]と[second]に一致する最初のレシピ
     */
    fun findFirstHolder(first: INPUT_A, second: INPUT_B, context: HTRecipeLookup.Context): HTRecipeHolder<RECIPE>? {
        if (isEmpty(first, second)) return null
        restoreLastRecipe(context)
        // 現在のキャッシュがレシピを保持している場合，現在の入力に一致するか判定
        lastRecipe?.onRight { holder: HTRecipeHolder<RECIPE> ->
            if (matches(holder.recipe, first, second)) {
                return holder
            } else {
                lastRecipe = null
            }
        }
        // キャッシュがない場合，すべてのレシピから検索する
        return lookup.getAllRecipes(context)
            .firstOrNull { (_, recipe: RECIPE) -> matches(recipe, first, second) }
            ?.also { lastRecipe = it.right() }
    }

    protected abstract fun isEmpty(first: INPUT_A, second: INPUT_B): Boolean

    /**
     * @since 26.1.13
     */
    protected abstract fun matches(recipe: RECIPE, first: INPUT_A, second: INPUT_B): Boolean
}
