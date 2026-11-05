package hiiragi283.lib.recipe.handler.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.util.right
import net.minecraft.core.TypedInstance
import net.minecraft.server.level.ServerLevel

/**
 * 単一の入力に対する[HTBaseRecipeCache]の拡張クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
abstract class HTSingleRecipeCache<INPUT : Any, RECIPE : Any>(lookup: HTRecipeLookup<RECIPE>) : HTBaseRecipeCache<RECIPE>(lookup) {
    /**
     * レシピを取得します。
     * @param input レシピの入力
     * @param level レシピ取得時のレベル
     * @return [input]に一致する最初のレシピ
     */
    fun findFirstRecipe(input: INPUT, level: ServerLevel): RECIPE? = findFirstRecipe(input, HTRecipeLookup.Context(level))

    /**
     * レシピを取得します。
     * @param input レシピの入力
     * @param context レシピ取得時のコンテキスト
     * @return [input]に一致する最初のレシピ
     */
    fun findFirstRecipe(input: INPUT, context: HTRecipeLookup.Context): RECIPE? = findFirstHolder(input, context)?.recipe

    /**
     * [HTRecipeHolder]を取得します。
     * @param input レシピの入力
     * @param context レシピ取得時のコンテキスト
     * @return [input]に一致する最初のレシピ
     */
    fun findFirstHolder(input: INPUT, context: HTRecipeLookup.Context): HTRecipeHolder<RECIPE>? {
        if (isEmpty(input)) return null
        restoreLastRecipe(context)
        // 現在のキャッシュがレシピを保持している場合，現在の入力に一致するか判定
        lastRecipe?.onRight { holder: HTRecipeHolder<RECIPE> ->
            if (matches(holder.recipe, input)) {
                return holder
            } else {
                lastRecipe = null
            }
        }
        // キャッシュがない場合，すべてのレシピから検索する
        return lookup.getAllRecipes(context)
            .firstOrNull { (_, recipe: RECIPE) -> matches(recipe, input) }
            ?.also { lastRecipe = it.right() }
    }

    protected abstract fun isEmpty(input: INPUT): Boolean

    /**
     * @since 26.1.13
     */
    protected abstract fun matches(recipe: RECIPE, input: INPUT): Boolean

    /**
     * 入力が[TypedInstance]を継承している場合に利用できる[HTSingleRecipeCache]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    abstract class Typed<TYPE : Any, INPUT : TypedInstance<TYPE>, RECIPE : Any>(
        lookup: HTRecipeLookup<RECIPE>,
        private val ingredientGetter: (RECIPE) -> HTIngredient<TYPE, INPUT>,
    ) : HTSingleRecipeCache<INPUT, RECIPE>(lookup) {
        fun getIngredient(recipe: RECIPE): HTIngredient<TYPE, INPUT> = ingredientGetter(recipe)

        final override fun matches(recipe: RECIPE, input: INPUT): Boolean = getIngredient(recipe).test(input)

        fun findAllHolder(input: TypedInstance<TYPE>, context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> =
            lookup.getAllRecipes(context).filter { (_, recipe: RECIPE) -> getIngredient(recipe).testOnlyType(input) }
    }
}
