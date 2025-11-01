package hiiragi283.ragium.api.data.lang

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.material.attribute.HTTranslatedNameMaterialAttribute
import hiiragi283.ragium.api.material.get
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import hiiragi283.ragium.setup.MekanismMaterialPrefixes

object HTMaterialTranslations {
    @JvmField
    val PREFIX_MAP: Map<HTMaterialPrefix, HTTranslationProvider> = buildMap {
        // Block
        this[CommonMaterialPrefixes.GLASS_BLOCK] = SimpleTranslation("%s Glass", "%sガラス")
        this[CommonMaterialPrefixes.GLASS_BLOCK_TINTED] = SimpleTranslation("Tinted %s Glass", "遮光%sガラス")
        this[CommonMaterialPrefixes.STORAGE_BLOCK] = SimpleTranslation("Block of %s", "%sブロック")
        this[CommonMaterialPrefixes.RAW_STORAGE_BLOCK] = SimpleTranslation("Block of Raw %s", "%sの原石ブロック")
        // Item
        this[CommonMaterialPrefixes.DUST] = SimpleTranslation("%s Dust", "%sの粉")
        this[CommonMaterialPrefixes.GEM] = SimpleTranslation("%s", "%s")
        this[CommonMaterialPrefixes.GEAR] = SimpleTranslation("%s Gear", "%sの歯車")
        this[CommonMaterialPrefixes.INGOT] = SimpleTranslation("%s Ingot", "%sインゴット")
        this[CommonMaterialPrefixes.NUGGET] = SimpleTranslation("%s Nugget", "%sナゲット")
        this[CommonMaterialPrefixes.PLATE] = SimpleTranslation("%s Plate", "%s板")
        this[CommonMaterialPrefixes.RAW_MATERIAL] = SimpleTranslation("Raw %s", "%sの原石")
        this[CommonMaterialPrefixes.ROD] = SimpleTranslation("%s Rod", "%s棒")

        this[CommonMaterialPrefixes.FUEL] = SimpleTranslation("%s", "%s")
        this[CommonMaterialPrefixes.SCRAP] = SimpleTranslation("%s Scrap", "%sの欠片")

        this[MekanismMaterialPrefixes.DIRTY_DUST] = SimpleTranslation("Dirty %s Dust", "汚れた%sの粉")
        this[MekanismMaterialPrefixes.CLUMP] = SimpleTranslation("%s Clump", "%sの塊")
        this[MekanismMaterialPrefixes.SHARD] = SimpleTranslation("%s Shard", "%sの欠片")
        this[MekanismMaterialPrefixes.CRYSTAL] = SimpleTranslation("%s Crystal", "%sの結晶")
        this[MekanismMaterialPrefixes.PELLET] = SimpleTranslation("%s Pellet", "%sペレット")
        this[MekanismMaterialPrefixes.ENRICHED] = SimpleTranslation("Enriched %s", "濃縮%s")
    }

    @JvmStatic
    val MATERIAL_MAP: ImmutableTable<HTMaterialPrefix, HTMaterialKey, HTTranslatedNameProvider> = buildTable {
        this[CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD] = HTTranslatedNameMaterialAttribute("Sawdust", "おがくず")
        this[CommonMaterialPrefixes.DUST, RagiumMaterialKeys.MEAT] = HTTranslatedNameMaterialAttribute("Minced Meat", "ひき肉")
    }

    fun getPrefixTranslation(prefix: HTMaterialPrefix): HTTranslationProvider? = PREFIX_MAP[prefix]

    fun getNameProvider(material: HTMaterialLike): HTTranslatedNameProvider? = (material as? HTTranslatedNameProvider)
        ?: RagiumPlatform.INSTANCE
            .getMaterialDefinition(material.asMaterialKey())
            .get<HTTranslatedNameMaterialAttribute>()

    fun translate(type: HTLanguageType, prefix: HTMaterialPrefix, key: HTMaterialKey): String? {
        val customName: HTTranslatedNameProvider? = MATERIAL_MAP[prefix, key]
        if (customName != null) {
            return customName.getTranslatedName(type)
        } else {
            val translation: HTTranslationProvider = PREFIX_MAP[prefix] ?: return null
            val translatedName: HTTranslatedNameProvider = getNameProvider(key) ?: return null
            return translation.translate(type, translatedName)
        }
    }

    private class SimpleTranslation(private val enPattern: String, private val jaPattern: String) : HTTranslationProvider {
        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        }.replace("%s", value)
    }
}
