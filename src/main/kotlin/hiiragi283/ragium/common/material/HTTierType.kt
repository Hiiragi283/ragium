package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialType
import net.minecraft.world.item.Rarity

enum class HTTierType(val rarity: Rarity) : HTMaterialType.Translatable {
    BASIC(Rarity.COMMON) {
        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Basic"
            HTLanguageType.JA_JP -> "基本"
        }
    },
    ADVANCED(Rarity.UNCOMMON) {
        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Advanced"
            HTLanguageType.JA_JP -> "発展"
        }
    },
    ELITE(Rarity.RARE) {
        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Elite"
            HTLanguageType.JA_JP -> "精鋭"
        }
    },
    ULTIMATE(Rarity.EPIC) {
        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Ultimate"
            HTLanguageType.JA_JP -> "究極"
        }
    },
    CREATIVE(Rarity.EPIC) {
        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> "Creative"
            HTLanguageType.JA_JP -> "クリエイティブ"
        }
    },
    ;

    companion object {
        @JvmField
        val COMMON: List<HTTierType> = listOf(
            BASIC,
            ADVANCED,
            ELITE,
            ULTIMATE,
        )
    }

    override fun getSerializedName(): String = name.lowercase()
}
