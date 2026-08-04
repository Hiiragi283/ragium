package hiiragi283.lib.text

import hiiragi283.lib.color.HTDefaultColor

/**
 * 翻訳を保持するインターフェースです。
 *
 * 参照 : [Mekanism - ILangEntry](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/text/ILangEntry.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTTranslation : HTHasTranslationKey {
    /**
     * [テキスト][Text]を返します。
     */
    fun translate(): MutableText = translatableText(this.translationKey)

    /**
     * [テキスト][Text]を返します。
     * @param args テキストの引数
     */
    fun translate(vararg args: Any?): MutableText = HTTextUtil.smartTranslate(this.translationKey, *args)

    /**
     * [color]で着色された[テキスト][Text]を返します。
     */
    fun translateColored(color: HTDefaultColor): MutableText = translate().withStyle(color)

    /**
     * [color]で着色された[テキスト][Text]を返します。
     * @param args テキストの引数
     */
    fun translateColored(color: HTDefaultColor, vararg args: Any?): MutableText = translate(*args).withStyle(color)
}
