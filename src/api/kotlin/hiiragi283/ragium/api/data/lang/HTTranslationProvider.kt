package hiiragi283.ragium.api.data.lang

fun interface HTTranslationProvider {
    fun translate(type: HTLanguageType, value: String): String
}
