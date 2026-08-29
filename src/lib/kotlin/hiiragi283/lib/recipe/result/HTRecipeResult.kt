package hiiragi283.lib.recipe.result

import hiiragi283.lib.resource.HTIdLike

/**
 * レシピの完成品を表すクラスです。
 * @param STACK 取得する完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTRecipeResult<STACK : Any> : HTIdLike {
    /**
     * 完成品を取得します。
     */
    fun create(): STACK
}
