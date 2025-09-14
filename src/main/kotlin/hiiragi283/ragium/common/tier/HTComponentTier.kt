package hiiragi283.ragium.common.tier

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTMaterialTier
import hiiragi283.ragium.api.tier.HTTierProvider

enum class HTComponentTier(private val base: HTBaseTier) : HTMaterialTier {
    BASIC(HTBaseTier.BASIC),
    ADVANCED(HTBaseTier.ADVANCED),
    ELITE(HTBaseTier.ELITE),
    ULTIMATE(HTBaseTier.ULTIMATE),
    ETERNAL(HTBaseTier.CREATIVE),
    ;

    companion object {
        @JvmStatic
        fun from(provider: HTTierProvider): HTComponentTier? =
            entries.firstOrNull { tier: HTComponentTier -> tier.getBaseTier() == provider.getBaseTier() }
    }

    override fun getBaseTier(): HTBaseTier = base

    override fun getTranslatedName(type: HTLanguageType): String = when (this) {
        ETERNAL -> when (type) {
            HTLanguageType.EN_US -> "Eternal"
            HTLanguageType.JA_JP -> "永久"
        }
        else -> base.getTranslatedName(type)
    }

    override fun getSerializedName(): String = name.lowercase()
}
