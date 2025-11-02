package hiiragi283.ragium.api.data.lang

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.material.attribute.HTLangNameMaterialAttribute
import hiiragi283.ragium.api.material.get
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import hiiragi283.ragium.setup.MekanismMaterialPrefixes

object HTMaterialTranslations {
    @JvmField
    val PREFIX_MAP: Map<HTMaterialPrefix, HTLangPatternProvider> = buildMap {
        // Block
        this[CommonMaterialPrefixes.GLASS_BLOCK] = HTSimpleLangPattern("%s Glass", "%sガラス")
        this[CommonMaterialPrefixes.GLASS_BLOCK_TINTED] = HTSimpleLangPattern("Tinted %s Glass", "遮光%sガラス")
        this[CommonMaterialPrefixes.STORAGE_BLOCK] = HTSimpleLangPattern("Block of %s", "%sブロック")
        this[CommonMaterialPrefixes.RAW_STORAGE_BLOCK] = HTSimpleLangPattern("Block of Raw %s", "%sの原石ブロック")
        // Item
        this[CommonMaterialPrefixes.DUST] = HTSimpleLangPattern("%s Dust", "%sの粉")
        this[CommonMaterialPrefixes.GEM] = HTSimpleLangPattern("%s", "%s")
        this[CommonMaterialPrefixes.GEAR] = HTSimpleLangPattern("%s Gear", "%sの歯車")
        this[CommonMaterialPrefixes.INGOT] = HTSimpleLangPattern("%s Ingot", "%sインゴット")
        this[CommonMaterialPrefixes.NUGGET] = HTSimpleLangPattern("%s Nugget", "%sナゲット")
        this[CommonMaterialPrefixes.PLATE] = HTSimpleLangPattern("%s Plate", "%s板")
        this[CommonMaterialPrefixes.RAW_MATERIAL] = HTSimpleLangPattern("Raw %s", "%sの原石")
        this[CommonMaterialPrefixes.ROD] = HTSimpleLangPattern("%s Rod", "%s棒")

        this[CommonMaterialPrefixes.FUEL] = HTSimpleLangPattern("%s", "%s")
        this[CommonMaterialPrefixes.SCRAP] = HTSimpleLangPattern("%s Scrap", "%sの欠片")

        this[MekanismMaterialPrefixes.DIRTY_DUST] = HTSimpleLangPattern("Dirty %s Dust", "汚れた%sの粉")
        this[MekanismMaterialPrefixes.CLUMP] = HTSimpleLangPattern("%s Clump", "%sの塊")
        this[MekanismMaterialPrefixes.SHARD] = HTSimpleLangPattern("%s Shard", "%sの欠片")
        this[MekanismMaterialPrefixes.CRYSTAL] = HTSimpleLangPattern("%s Crystal", "%sの結晶")
        this[MekanismMaterialPrefixes.PELLET] = HTSimpleLangPattern("%s Pellet", "%sペレット")
        this[MekanismMaterialPrefixes.ENRICHED] = HTSimpleLangPattern("Enriched %s", "濃縮%s")
    }

    @JvmStatic
    val MATERIAL_MAP: ImmutableTable<HTMaterialPrefix, HTMaterialKey, HTLangName> = buildTable {
        this[CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD] = HTSimpleLangName("Sawdust", "おがくず")
        this[CommonMaterialPrefixes.DUST, RagiumMaterialKeys.MEAT] = HTSimpleLangName("Minced Meat", "ひき肉")
    }

    fun getLangName(material: HTMaterialLike): HTLangName? = (material as? HTLangName)
        ?: RagiumPlatform.INSTANCE
            .getMaterialDefinition(material.asMaterialKey())
            .get<HTLangNameMaterialAttribute>()

    fun translate(type: HTLanguageType, prefix: HTMaterialPrefix, key: HTMaterialKey): String? {
        val customName: HTLangName? = MATERIAL_MAP[prefix, key]
        if (customName != null) {
            return customName.getTranslatedName(type)
        } else {
            val translation: HTLangPatternProvider = PREFIX_MAP[prefix] ?: return null
            val translatedName: HTLangName = getLangName(key) ?: return null
            return translation.translate(type, translatedName)
        }
    }
}
