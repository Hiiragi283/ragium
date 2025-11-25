package hiiragi283.ragium.api.material.attribute

import hiiragi283.ragium.api.data.lang.HTLangName
import hiiragi283.ragium.api.data.lang.HTLanguageType

/**
 * 素材の翻訳名を保持する属性のクラス
 */
@JvmRecord
data class HTLangNameMaterialAttribute(private val enName: String, private val jaName: String) :
    HTMaterialAttribute,
    HTLangName {
    constructor(delegate: HTLangName) : this(
        delegate.getTranslatedName(HTLanguageType.EN_US),
        delegate.getTranslatedName(HTLanguageType.JA_JP),
    )

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jaName
    }
}
