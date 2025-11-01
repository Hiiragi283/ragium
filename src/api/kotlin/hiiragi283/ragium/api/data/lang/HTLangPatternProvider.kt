package hiiragi283.ragium.api.data.lang

/**
 * 受け取った文字列で`%s`を置換した文字列を返す関数型インターフェース
 */
fun interface HTLangPatternProvider {
    /**
     * @param type 言語の種類
     * @param value `%s`を置換する文字列
     * @return '%s'が置換された翻訳名
     */
    fun translate(type: HTLanguageType, value: String): String

    /**
     * @param type 言語の種類
     * @param provider `%s`を置換する翻訳
     * @return '%s'が置換された翻訳名
     */
    fun translate(type: HTLanguageType, provider: HTLangName): String = translate(type, provider.getTranslatedName(type))
}
