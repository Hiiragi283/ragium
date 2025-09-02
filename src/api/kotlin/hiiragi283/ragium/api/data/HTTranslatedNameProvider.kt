package hiiragi283.ragium.api.data

fun interface HTTranslatedNameProvider {
    fun getTranslatedName(type: HTLanguageType): String
}
