package hiiragi283.lib.recipe.handler.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.util.right
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.common.util.TriPredicate

/**
 * 3つの入力に対する[HTBaseRecipeCache]の拡張クラスです。
 * @param INPUT_A 1番目のレシピの入力となるクラス
 * @param INPUT_B 2番目のレシピの入力となるクラス
 * @param INPUT_C 3番目のレシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
abstract class HTTripleRecipeCache<INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, RECIPE : TriPredicate<INPUT_A, INPUT_B, INPUT_C>>(
    lookup: HTRecipeLookup<RECIPE>,
) : HTBaseRecipeCache<RECIPE>(lookup) {
    /**
     * レシピを取得します。
     * @param first レシピの1番目の入力
     * @param second レシピの2番目の入力
     * @param third レシピの3番目の入力
     * @param level レシピ取得時のレベル
     * @return [first]と[second]に一致する最初のレシピ
     */
    fun findFirstRecipe(first: INPUT_A, second: INPUT_B, third: INPUT_C, level: ServerLevel): RECIPE? =
        findFirstRecipe(first, second, third, HTRecipeLookup.Context(level))

    /**
     * レシピを取得します。
     * @param first レシピの1番目の入力
     * @param second レシピの2番目の入力
     * @param third レシピの3番目の入力
     * @param context レシピ取得時のコンテキスト
     * @return [first]と[second]に一致する最初のレシピ
     */
    fun findFirstRecipe(first: INPUT_A, second: INPUT_B, third: INPUT_C, context: HTRecipeLookup.Context): RECIPE? =
        findFirstHolder(first, second, third, context)?.recipe

    /**
     * [HTRecipeHolder]を取得します。
     * @param first レシピの1番目の入力
     * @param second レシピの2番目の入力
     * @param third レシピの3番目の入力
     * @param context レシピ取得時のコンテキスト
     * @return [first]と[second]に一致する最初のレシピ
     */
    fun findFirstHolder(first: INPUT_A, second: INPUT_B, third: INPUT_C, context: HTRecipeLookup.Context): HTRecipeHolder<RECIPE>? {
        if (isEmpty(first, second, third)) return null
        restoreLastRecipe(context)
        // 現在のキャッシュがレシピを保持している場合，現在の入力に一致するか判定
        lastRecipe?.onRight { holder: HTRecipeHolder<RECIPE> ->
            if (holder.recipe.test(first, second, third)) {
                return holder
            } else {
                lastRecipe = null
            }
        }
        // キャッシュがない場合，すべてのレシピから検索する
        return lookup.getAllRecipes(context)
            .firstOrNull { (_, recipe: RECIPE) -> recipe.test(first, second, third) }
            ?.also { lastRecipe = it.right() }
    }

    protected abstract fun isEmpty(first: INPUT_A, second: INPUT_B, third: INPUT_C): Boolean
}
