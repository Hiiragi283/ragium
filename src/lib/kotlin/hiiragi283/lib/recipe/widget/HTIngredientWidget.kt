package hiiragi283.lib.recipe.widget

/**
 * レシピビューワーに保持しているオブジェクトを提供可能なウィジェットを表すインターフェースです。
 *
 * 参照 : [Mekanism - IRecipeViewerGhostTarget](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/recipe_viewer/interfaces/IRecipeViewerIngredientHelper.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTIngredientWidget {
    /**
     * 保持しているオブジェクトを取得します。
     * @return 空の場合は`null`
     */
    fun getIngredient(): Any?
}
