package hiiragi283.ragium.common.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.data.texture.HTColorPalette
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.common.data.texture.RagiumMaterialPalette
import net.minecraft.resources.ResourceLocation

/**
 * @see hiiragi283.core.common.material.HCMaterial
 */
enum class RagiumMaterial(private val usName: String, private val jpName: String, val colorPalette: HTColorPalette) :
    HTMaterialLike,
    HTLangName {
    // Mineral
    RAGINITE("Raginite", "ラギナイト", RagiumMaterialPalette.RAGINITE),

    // Gem
    RAGI_CRYSTAL("Ragi-Crystal", "ラギクリスタル", RagiumMaterialPalette.RAGI_CRYSTAL),

    // Alloy
    RAGI_ALLOY("Ragi-Alloy", "ラギ合金", RagiumMaterialPalette.RAGINITE),
    ADVANCED_RAGI_ALLOY("Advanced Ragi-Alloy", "発展ラギ合金", RagiumMaterialPalette.ADVANCED_RAGI_ALLOY),
    ;

    val basePrefix: HTMaterialPrefix
        get() = when (this) {
            RAGINITE -> HCMaterialPrefixes.DUST
            RAGI_CRYSTAL -> HCMaterialPrefixes.GEM
            else -> HCMaterialPrefixes.INGOT
        }

    fun getItemPrefixesToGenerate(): Set<HTMaterialPrefix> = when (this) {
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

    fun getTemplateId(prefix: HTMaterialPrefix): ResourceLocation? {
        val name: String = when (prefix) {
            HCMaterialPrefixes.STORAGE_BLOCK -> "block_${basePrefix.name}"
            HCMaterialPrefixes.DUST -> when (this) {
                RAGINITE -> "dust_dull"
                else -> prefix.name
            }
            HCMaterialPrefixes.GEM -> null
            else -> prefix.name
        } ?: return null
        return HiiragiCoreAPI.id("template", name)
    }

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(this.name.lowercase())

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> usName
        HTLanguageType.JA_JP -> jpName
    }
}
