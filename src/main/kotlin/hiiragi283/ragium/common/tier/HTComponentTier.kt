package hiiragi283.ragium.common.tier

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTMaterialTier
import hiiragi283.ragium.api.tier.HTTierProvider

enum class HTComponentTier(private val base: HTBaseTier) : HTMaterialTier {
    ADVANCED(HTBaseTier.ADVANCED),
    ELITE(HTBaseTier.ELITE),
    ULTIMATE(HTBaseTier.ULTIMATE),
    ;

    companion object {
        @JvmStatic
        fun from(provider: HTTierProvider): HTComponentTier? =
            entries.firstOrNull { tier: HTComponentTier -> tier.getBaseTier() == provider.getBaseTier() }
    }

    override fun getBaseTier(): HTBaseTier = base

    override fun getTranslatedName(type: HTLanguageType): String = base.getTranslatedName(type)

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
}
