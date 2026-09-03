package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.id
import hiiragi283.lib.recipe.recipe
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [HTRecipeLookup]に基づいて，取得したレシピをキャッシュするクラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @param lookup レシピの提供元
 */
class HTRecipeCache<INPUT : RecipeInput, RECIPE : HTRecipePredicate<INPUT>>(
    private val lookup: HTRecipeLookup<RECIPE>
) {
    private var lastRecipe: HTRecipeHolder<RECIPE>? = null

    /**
     * レシピを取得します。
     * @param input レシピの入力
     * @return [input]に一致する最初のレシピ
     * @since 21.1.1
     */
    fun findFirstRecipe(input: INPUT): RECIPE? = findFirstRecipe(input, HTPhysicalSideHelper.createLookupContext())

    /**
     * レシピを取得します。
     * @param input レシピの入力
     * @param level レシピ取得時のレベル
     * @return [input]に一致する最初のレシピ
     */
    fun findFirstRecipe(input: INPUT, level: ServerLevel): RECIPE? =
        findFirstRecipe(input, HTRecipeLookup.Context(level))

    /**
     * レシピを取得します。
     * @param input レシピの入力
     * @param context レシピ取得時のコンテキスト
     * @return [input]に一致する最初のレシピ
     */
    fun findFirstRecipe(input: INPUT, context: HTRecipeLookup.Context): RECIPE? =
        findFirstHolder(input, context)?.recipe

    /**
     * [HTRecipeHolder]を取得します。
     * @param input レシピの入力
     * @param context レシピ取得時のコンテキスト
     * @return [input]に一致する最初のレシピ
     */
    fun findFirstHolder(input: INPUT, context: HTRecipeLookup.Context): HTRecipeHolder<RECIPE>? {
        if (input.isEmpty) return null
        if (lastRecipe != null && lastRecipe!!.recipe.matches(input)) {
            return lastRecipe
        }
        lookup.getAllRecipesN(context)
            .firstOrNull { (_, recipe) -> recipe.matches(input) }
            ?.let(::lastRecipe::set)
        return lastRecipe
    }

    override fun toString(): String = "HTRecipeCache(lookup=$lookup, lastRecipe=${lastRecipe?.id})"
}
