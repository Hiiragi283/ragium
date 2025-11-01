package hiiragi283.ragium.api.material.attribute

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslatedNameProvider

@JvmRecord
data class HTTranslatedNameMaterialAttribute(private val enName: String, private val jaName: String) :
    HTMaterialAttribute,
    HTTranslatedNameProvider {
    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jaName
    }
}
