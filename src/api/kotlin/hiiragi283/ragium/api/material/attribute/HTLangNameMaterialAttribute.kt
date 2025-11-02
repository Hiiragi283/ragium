package hiiragi283.ragium.api.material.attribute

import hiiragi283.ragium.api.data.lang.HTLangName
import hiiragi283.ragium.api.data.lang.HTLanguageType

@JvmRecord
data class HTLangNameMaterialAttribute(private val enName: String, private val jaName: String) :
    HTMaterialAttribute,
    HTLangName {
    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jaName
    }
}
