package hiiragi283.lib.data.lang

/**
 * 受け取った文字列で`%s`を置換した文字列を返す処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTLangPatternProvider {
    /**
     * @param type 言語の種類
     * @param value `%s`を置換する文字列
     * @return '%s'が置換された翻訳名
     */
    fun translate(type: HTLangType, value: String): String

    /**
     * @param type 言語の種類
     * @param provider `%s`を置換する翻訳
     * @return '%s'が置換された翻訳名
     */
    fun translate(type: HTLangType, provider: HTLangName): String = translate(type, provider.getTranslatedName(type))

    companion object {
        /**
         * @since 21.1.0
         */
        @JvmField
        val IDENTITY: HTLangPatternProvider = IdentityLangPatternProvider
    }
}

fun HTLangPatternProvider(enPattern: String, jaPattern: String): HTLangPatternProvider = EnJaLangPatternProvider(enPattern, jaPattern)

private data object IdentityLangPatternProvider : HTLangPatternProvider {
    override fun translate(type: HTLangType, value: String): String = value
}

@JvmRecord
private data class EnJaLangPatternProvider(private val enPattern: String, private val jaPattern: String) : HTLangPatternProvider {
    override fun translate(type: HTLangType, value: String): String = when (type) {
        HTLangTypes.JA_JP -> jaPattern
        else -> enPattern
    }.replace("%s", value)
}
