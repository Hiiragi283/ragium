package hiiragi283.ragium.api.recipe.manager

import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * レシピを保持するキャッシュのクラス
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 */
interface HTRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> {
    /**
     * 指定された[input], [level]から最初に一致するレシピを返します。
     * @param input レシピの入力
     * @param level レシピを取得するレベル
     * @return 見つからなかった場合は`null`
     */
    fun getFirstRecipe(input: INPUT, level: Level): RECIPE? = getFirstHolder(input, level)?.value

    /**
     * 指定された[input], [level]から最初に一致する[RecipeHolder]を返します。
     * @param input レシピの入力
     * @param level レシピを取得するレベル
     * @return 見つからなかった場合は`null`
     */
    fun getFirstHolder(input: INPUT, level: Level): RecipeHolder<RECIPE>?
}
