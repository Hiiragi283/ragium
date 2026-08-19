package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.RecipeKey
import net.minecraft.util.context.ContextMap

/**
 * レシピの一覧を提供するインターフェースです。
 *
 * 参照 : [Mekanism - IMekanismRecipeTypeProvider](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/recipe/IMekanismRecipeTypeProvider.java)
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTRecipeLookup<out RECIPE> {
    /**
     * レシピの一覧を取得します。
     * @param contextMap レシピのコンテキスト
     */
    fun getAllRecipes(contextMap: ContextMap): Map<RecipeKey, RECIPE>
}
