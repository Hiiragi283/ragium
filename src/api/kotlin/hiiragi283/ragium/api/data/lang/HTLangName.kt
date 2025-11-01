package hiiragi283.ragium.api.data.lang

/**
 * 翻訳した文字列を返す関数型インターフェース
 */
fun interface HTLangName {
    fun getTranslatedName(type: HTLanguageType): String
}
