package hiiragi283.lib.recipe.result

import net.minecraft.resources.Identifier

/**
 * レシピの完成品を表すクラスです。
 * @param STACK 取得する完成品のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTRecipeResult<STACK : Any> {
    fun getId(): Identifier

    /**
     * 完成品を取得します。
     */
    fun create(): STACK
}
