package hiiragi283.lib.data.lang

/**
 * 翻訳名を返す処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTLangName {
    /**
     * 指定された[言語の種類][type]から翻訳名を返します。
     */
    fun getTranslatedName(type: HTLangType): String

    companion object {
        /**
         * 新しい[HTLangName]のインスタンスを作成します。
         * @param enName 英語での翻訳名
         * @param jaName 日本語での翻訳名
         */
        @JvmStatic
        fun create(enName: String, jaName: String): HTLangName = HTLangName { type: HTLangType ->
            when (type) {
                HTLangTypes.JA_JP -> jaName
                else -> enName
            }
        }

        /**
         * 新しい[HTLangName]のインスタンスを作成します。
         * @param enName 英語での翻訳名
         * @param others 英語以外での翻訳名
         */
        @JvmStatic
        fun create(enName: String, vararg others: Pair<HTLangType, String>): HTLangName = create(enName, mapOf(*others))

        /**
         * 新しい[HTLangName]のインスタンスを作成します。
         * @param enName 英語での翻訳名
         * @param others 英語以外での翻訳名
         */
        @JvmStatic
        fun create(enName: String, others: Map<HTLangType, String>): HTLangName = HTLangName { type: HTLangType -> others[type] ?: enName }
    }
}
