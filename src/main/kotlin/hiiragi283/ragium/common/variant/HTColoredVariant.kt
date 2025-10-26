package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslationProvider
import hiiragi283.ragium.api.variant.HTVariantKey

enum class HTColoredVariant(private val enPattern: String, private val jaPattern: String) :
    HTVariantKey,
    HTTranslationProvider {
    WOOL("%s Wool", "%sの羊毛"),
    CONCRETE_POWDER("%s Concrete Powder", "%sのコンクリートパウダー"),
    CONCRETE("%s Concrete", "%sのコンクリート"),
    ;

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
