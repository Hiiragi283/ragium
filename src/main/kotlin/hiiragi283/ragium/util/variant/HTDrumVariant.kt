package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTVariantKey

enum class HTDrumVariant(private val enUsPattern: String, private val jaJpPattern: String) : HTVariantKey {
    SMALL("Small Drum", "ドラム（小）"),
    MEDIUM("Medium Drum", "ドラム（中）"),
    LARGE("Large Drum", "ドラム（大）"),
    HUGE("Huge Drum", "ドラム（特大）"),
    ;

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
