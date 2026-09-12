package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.key
import hiiragi283.lib.recipe.recipe
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.unwrap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable

/**
 * [HTRecipeLookup]に基づいて，取得したレシピをキャッシュするクラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @param lookup レシピの提供元
 */
class HTRecipeCache<INPUT : RecipeInput, RECIPE : HTRecipePredicate<INPUT>>(
    private val lookup: HTRecipeLookup<RECIPE>
) : ValueIOSerializable {
    private var lastRecipe: Either<RecipeKey, HTRecipeHolder<RECIPE>>? = null

    /**
     * レシピを取得します。
     * @param input レシピの入力
     * @return [input]に一致する最初のレシピ
     * @since 26.1.1
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
        // 現在のキャッシュがレシピIDのみの場合，lookupからレシピを検索して更新
        lastRecipe?.onLeft { lastKey: RecipeKey ->
            lookup.getAllRecipes(context)
                .firstOrNull { (key: RecipeKey, _) -> lastKey == key }
                ?.let { Either.Right(it) }
                .let { lastRecipe = it }
        }
        // 現在のキャッシュがレシピを保持している場合，現在の入力に一致するか判定
        lastRecipe?.onRight { holder: HTRecipeHolder<RECIPE> ->
            if (holder.recipe.matches(input)) {
                return holder
            } else {
                lastRecipe = null
            }
        }
        // キャッシュがない場合，すべてのレシピから検索する
        return lookup.getAllRecipes(context)
            .firstOrNull { (_, recipe: RECIPE) -> recipe.matches(input) }
            ?.also { lastRecipe = Either.Right(it) }
    }

    override fun serialize(output: ValueOutput) {
        output.storeNullable(HTConstants.ID, Recipe.KEY_CODEC, getRecipeKey())
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConstants.ID, Recipe.KEY_CODEC).ifPresent { lastRecipe = Either.Left(it) }
    }

    private fun getRecipeKey(): RecipeKey? = lastRecipe?.map(HTRecipeHolder<*>::key)?.unwrap()

    override fun toString(): String = "HTRecipeCache(lookup=$lookup, lastRecipe=${getRecipeKey()})"
}
