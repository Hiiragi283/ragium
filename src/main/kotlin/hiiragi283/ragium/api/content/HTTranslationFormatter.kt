package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.data.HTLangType

fun interface HTTranslationFormatter {
    fun getPattern(type: HTLangType): String

    fun getTranslation(type: HTLangType, provider: HTTranslationProvider): String =
        getPattern(type).replace("%s", provider.getTranslation(type))
}
