package hiiragi283.lib.recipe.handler.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.util.right
import net.minecraft.core.TypedInstance

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

    /**
     * 入力が[TypedInstance]を継承している場合に利用できる[HTDoubleRecipeCache]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    abstract class Typed<TYPE_A : Any, INPUT_A : TypedInstance<TYPE_A>, TYPE_B : Any, INPUT_B : TypedInstance<TYPE_B>, RECIPE : Any>(
        lookup: HTRecipeLookup<RECIPE>,
    ) : HTDoubleRecipeCache<INPUT_A, INPUT_B, RECIPE>(lookup) {
        abstract fun findAllHolderForFirst(input: TypedInstance<TYPE_A>, context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>>

        abstract fun findAllHolderForSecond(input: TypedInstance<TYPE_B>, context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>>
    }

    /**
     * 入力が[TypedInstance]を継承している場合に利用できる[Typed]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    abstract class SimpleTyped<TYPE_A : Any, INPUT_A : TypedInstance<TYPE_A>, TYPE_B : Any, INPUT_B : TypedInstance<TYPE_B>, RECIPE : Any>(
        lookup: HTRecipeLookup<RECIPE>,
        private val firstGetter: (RECIPE) -> HTIngredient<TYPE_A, INPUT_A>,
        private val secondGetter: (RECIPE) -> HTIngredient<TYPE_B, INPUT_B>,
    ) : Typed<TYPE_A, INPUT_A, TYPE_B, INPUT_B, RECIPE>(lookup) {
        fun getFirstIngredient(recipe: RECIPE): HTIngredient<TYPE_A, INPUT_A> = firstGetter(recipe)

        fun getSecondIngredient(recipe: RECIPE): HTIngredient<TYPE_B, INPUT_B> = secondGetter(recipe)

        override fun matches(recipe: RECIPE, first: INPUT_A, second: INPUT_B): Boolean =
            getFirstIngredient(recipe).test(first) && getSecondIngredient(recipe).test(second)

        override fun findAllHolderForFirst(input: TypedInstance<TYPE_A>, context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> =
            lookup.getAllRecipes(context).filter { (_, recipe: RECIPE) -> getFirstIngredient(recipe).testOnlyType(input) }

        override fun findAllHolderForSecond(input: TypedInstance<TYPE_B>, context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> =
            lookup.getAllRecipes(context).filter { (_, recipe: RECIPE) -> getSecondIngredient(recipe).testOnlyType(input) }
    }
}
