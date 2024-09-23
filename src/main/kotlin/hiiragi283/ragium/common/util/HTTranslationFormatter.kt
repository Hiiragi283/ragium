package hiiragi283.ragium.common.util

import hiiragi283.ragium.common.data.HTLangType

interface HTTranslationFormatter {
    val enPattern: String
    val jaPattern: String

    fun getTranslation(type: HTLangType, provider: HTTranslationProvider): String = when (type) {
        HTLangType.EN_US -> enPattern
        HTLangType.JA_JP -> jaPattern
    }.replace("%s", provider.getTranslation(type))
}
