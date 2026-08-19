package hiiragi283.lib.recipe.ingredient

import net.minecraft.util.context.ContextMap

/**
 * Hiiragi Seriesで使用される，材料のプレビューを提供するインターフェースです。
 * @param STACK 一致する材料のプレビューに使用されるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTStackPreview<STACK : Any> {
    /**
     * 一致する材料のプレビューを取得します。
     */
    fun getPreviewStacks(contextMap: ContextMap): List<STACK>
}
