package hiiragi283.lib.text

/**
 * [テキスト][Text]を提供するインターフェースです。
 *
 * 参照 : [Mekanism - IHasTextComponent](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/text/IHasTextComponent.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTHasText {
    /**
     * [テキスト][Text]を取得します。
     */
    fun getText(): Text
}
