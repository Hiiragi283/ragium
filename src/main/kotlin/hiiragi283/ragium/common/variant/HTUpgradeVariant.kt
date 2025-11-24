package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.variant.HTVariantKey

enum class HTUpgradeVariant(private val enPattern: String, private val jaPattern: String) : HTVariantKey {
    EFFICIENCY("%s Efficiency", "%s効率"),
    ENERGY_CAPACITY("%s Energy Capacity", "%sエネルギー容量"),
    SPEED("%s Speed", "%sスピード"),
    ;

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "$enPattern Upgrade"
        HTLanguageType.JA_JP -> "${jaPattern}アップグレード"
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
