package hiiragi283.ragium.common.tier

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTTierProvider

enum class HTCircuitTier(private val base: HTBaseTier) :
    HTMaterialLike.Translatable,
    HTTierProvider {
    BASIC(HTBaseTier.BASIC),
    ADVANCED(HTBaseTier.ADVANCED),
    ELITE(HTBaseTier.ELITE),
    ULTIMATE(HTBaseTier.ULTIMATE),
    ;

    override fun getBaseTier(): HTBaseTier = base

    override fun getTranslatedName(type: HTLanguageType): String = base.getTranslatedName(type)

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())
}
