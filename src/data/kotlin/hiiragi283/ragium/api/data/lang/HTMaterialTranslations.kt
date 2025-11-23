package hiiragi283.ragium.api.data.lang

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.attribute.HTLangNameMaterialAttribute
import hiiragi283.ragium.api.material.get
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.MekanismMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys

object HTMaterialTranslations {
    @JvmField
    val DEFAULT_PREFIX_PATTERN = HTSimpleLangPattern("%s", "%s")

    @JvmField
    val PREFIX_MAP: Map<HTMaterialPrefix, HTLangPatternProvider> = buildMap {
        fun register(prefix: HTPrefixLike, enPattern: String, jaPattern: String) {
            this[prefix.asMaterialPrefix()] = HTSimpleLangPattern(enPattern, jaPattern)
        }

        // Block
        register(CommonMaterialPrefixes.STORAGE_BLOCK, "Block of %s", "%sブロック")
        register(CommonMaterialPrefixes.RAW_STORAGE_BLOCK, "Block of Raw %s", "%sの原石ブロック")
        // Item
        register(CommonMaterialPrefixes.DUST, "%s Dust", "%sの粉")
        register(CommonMaterialPrefixes.GEAR, "%s Gear", "%sの歯車")
        register(CommonMaterialPrefixes.INGOT, "%s Ingot", "%sインゴット")
        register(CommonMaterialPrefixes.NUGGET, "%s Nugget", "%sナゲット")
        register(CommonMaterialPrefixes.PLATE, "%s Plate", "%s板")
        register(CommonMaterialPrefixes.RAW_MATERIAL, "Raw %s", "%sの原石")
        register(CommonMaterialPrefixes.ROD, "%s Rod", "%s棒")

        register(CommonMaterialPrefixes.DOUGH, "%s Dough", "%s粉の生地")
        register(CommonMaterialPrefixes.FLOUR, "%s Flour", "%s粉")
        register(CommonMaterialPrefixes.FUEL, "%s", "%s")
        register(CommonMaterialPrefixes.SCRAP, "%s Scrap", "%sの欠片")

        register(MekanismMaterialPrefixes.DIRTY_DUST, "Dirty %s Dust", "汚れた%sの粉")
        register(MekanismMaterialPrefixes.CLUMP, "%s Clump", "%sの塊")
        register(MekanismMaterialPrefixes.SHARD, "%s Shard", "%sの欠片")
        register(MekanismMaterialPrefixes.CRYSTAL, "%s Crystal", "%sの結晶")
        register(MekanismMaterialPrefixes.PELLET, "%s Pellet", "%sペレット")
        register(MekanismMaterialPrefixes.ENRICHED, "Enriched %s", "濃縮%s")
    }

    @JvmStatic
    val MATERIAL_MAP: ImmutableTable<HTMaterialPrefix, HTMaterialKey, HTLangName> = buildTable {
        fun register(
            prefix: HTPrefixLike,
            material: HTMaterialLike,
            enName: String,
            jaName: String,
        ) {
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = HTSimpleLangName(enName, jaName)
        }

        register(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, "Sawdust", "おがくず")
        register(CommonMaterialPrefixes.DUST, FoodMaterialKeys.RAW_MEAT, "Minced Meat", "ひき肉")
    }

    fun getLangName(material: HTMaterialLike): HTLangName? = (material as? HTLangName)
        ?: RagiumPlatform.INSTANCE
            .getMaterialDefinition(material.asMaterialKey())
            .get<HTLangNameMaterialAttribute>()

    fun translate(type: HTLanguageType, prefix: HTPrefixLike, key: HTMaterialKey): String? {
        val customName: HTLangName? = MATERIAL_MAP[prefix.asMaterialPrefix(), key]
        if (customName != null) {
            return customName.getTranslatedName(type)
        } else {
            val translation: HTLangPatternProvider = PREFIX_MAP[prefix] ?: DEFAULT_PREFIX_PATTERN
            val translatedName: HTLangName = getLangName(key) ?: return null
            return translation.translate(type, translatedName)
        }
    }
}
