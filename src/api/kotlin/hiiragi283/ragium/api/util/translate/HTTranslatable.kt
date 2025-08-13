package hiiragi283.ragium.api.util.translate

import hiiragi283.ragium.api.data.HTLanguageType

fun interface HTTranslatable {
    fun getTranslatedName(type: HTLanguageType): String
}
