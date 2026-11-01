package hiiragi283.lib.text

import net.minecraft.ChatFormatting

/**
 * 翻訳を保持するインターフェースです。
 *
 * 参照 : [Mekanism - ILangEntry](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/text/ILangEntry.java)
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
     * [テキスト][Text]を返します。
     * @param args テキストの引数
     * @since 26.1.13
     */
    fun translate(args: Iterable<Any?>): MutableText = HTTextUtil.smartTranslate(this.translationKey, args)

    /**
     * [color]で着色された[テキスト][Text]を返します。
     */
    fun translateColored(color: ChatFormatting): MutableText = translate().withStyle(color)

    /**
     * [color]で着色された[テキスト][Text]を返します。
     * @param args テキストの引数
     */
    fun translateColored(color: ChatFormatting, vararg args: Any?): MutableText = translate(*args).withStyle(color)

    /**
     * [color]で着色された[テキスト][Text]を返します。
     * @param args テキストの引数
     * @since 26.1.13
     */
    fun translateColored(color: ChatFormatting, args: Iterable<Any?>): MutableText = translate(args).withStyle(color)
}
