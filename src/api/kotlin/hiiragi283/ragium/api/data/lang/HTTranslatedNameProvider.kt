package hiiragi283.ragium.api.data.lang

fun interface HTTranslatedNameProvider {
    fun getTranslatedName(type: HTLanguageType): String
}
