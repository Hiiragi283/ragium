package hiiragi283.ragium.common.material

import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.material.HTAbstractMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixTemplateMap
import hiiragi283.core.common.data.texture.HCMaterialPrefixMaps
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.common.data.texture.RagiumMaterialPalette

/**
 * @see hiiragi283.core.common.material.HCMaterial
 */
enum class RagiumMaterial(private val usName: String, private val jpName: String, override val colorPalette: HTColorPalette) :
    HTAbstractMaterial {
    // Mineral
    RAGINITE("Raginite", "ラギナイト", RagiumMaterialPalette.RAGINITE),

    // Gem
    RAGI_CRYSTAL("Ragi-Crystal", "ラギクリスタル", RagiumMaterialPalette.RAGINITE),

    // Alloy
    RAGI_ALLOY("Ragi-Alloy", "ラギ合金", RagiumMaterialPalette.RAGINITE),
    ADVANCED_RAGI_ALLOY("Advanced Ragi-Alloy", "発展ラギ合金", RagiumMaterialPalette.ADVANCED_RAGI_ALLOY),
    ;

    override val basePrefix: HTMaterialPrefix
        get() = when (this) {
            RAGINITE -> HCMaterialPrefixes.DUST
            RAGI_CRYSTAL -> HCMaterialPrefixes.GEM
            else -> HCMaterialPrefixes.INGOT
        }

    override fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = when (this) {
        RAGINITE -> setOf(HCMaterialPrefixes.DUST, HCMaterialPrefixes.TINY_DUST)
        RAGI_CRYSTAL -> setOf(HCMaterialPrefixes.GEM, HCMaterialPrefixes.DUST, HCMaterialPrefixes.TINY_DUST)
        else -> setOf(
            HCMaterialPrefixes.INGOT,
            HCMaterialPrefixes.DUST,
            HCMaterialPrefixes.TINY_DUST,
            HCMaterialPrefixes.GEAR,
            HCMaterialPrefixes.NUGGET,
            HCMaterialPrefixes.PLATE,
            HCMaterialPrefixes.ROD,
            HCMaterialPrefixes.WIRE,
        )
    }

    override fun getItemPrefixMap(): HTPrefixTemplateMap = when (this) {
        RAGINITE -> HCMaterialPrefixMaps.DUST_DULL
        RAGI_CRYSTAL -> HCMaterialPrefixMaps.GEM_DIAMOND
        RAGI_ALLOY -> HCMaterialPrefixMaps.METAL
        ADVANCED_RAGI_ALLOY -> HCMaterialPrefixMaps.METAL
    }

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> usName
        HTLanguageType.JA_JP -> jpName
    }
}
