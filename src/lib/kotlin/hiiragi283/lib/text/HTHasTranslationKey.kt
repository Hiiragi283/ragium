package hiiragi283.lib.text

/**
 * 翻訳キーを提供するインターフェースです。
 *
 * 参照 : [Mekanism - IHasTranslationKey](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/text/IHasTranslationKey.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTHasTranslationKey {
    /**
     * 翻訳キーの値
     */
    val translationKey: String
}
