package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.variant.HTVariantKey

enum class HTGlassVariant(private val enPattern: String, private val jaPattern: String) : HTVariantKey {
    DEFAULT("%s Glass", "%sガラス"),
    TINTED("Tinted %s Glass", "遮光%sガラス"),
    ;

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
