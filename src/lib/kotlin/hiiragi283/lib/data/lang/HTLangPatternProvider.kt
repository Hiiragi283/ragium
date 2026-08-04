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
        @JvmField
        val IDENTITY = HTLangPatternProvider { _, value -> value }

        /**
         * 新しい[HTLangPatternProvider]のインスタンスを作成します。
         * @param enPattern 英語での翻訳名のパターン
         * @param jaPattern 日本語での翻訳名のパターン
         */
        @JvmStatic
        fun create(enPattern: String, jaPattern: String): HTLangPatternProvider = HTLangPatternProvider { type: HTLangType, value: String ->
            when (type) {
                HTLangTypes.JA_JP -> jaPattern
                else -> enPattern
            }.replace("%s", value)
        }

        /**
         * 新しい[HTLangPatternProvider]のインスタンスを作成します。
         * @param enPattern 英語での翻訳名のパターン
         * @param others 英語以外での翻訳名のパターン
         */
        @JvmStatic
        fun create(enPattern: String, vararg others: Pair<HTLangType, String>): HTLangPatternProvider = create(enPattern, mapOf(*others))

        /**
         * 新しい[HTLangPatternProvider]のインスタンスを作成します。
         * @param enPattern 英語での翻訳名のパターン
         * @param others 英語以外での翻訳名のパターン
         */
        @JvmStatic
        fun create(enPattern: String, others: Map<HTLangType, String>): HTLangPatternProvider = HTLangPatternProvider { type: HTLangType, value: String -> (others[type] ?: enPattern).replace("%s", value) }
    }
}
