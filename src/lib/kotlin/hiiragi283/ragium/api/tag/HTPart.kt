package hiiragi283.ragium.api.tag

import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed interface HTPart : HTLangPatternProvider {
    val tagPrefix: HTTagPrefix

    fun createName(material: HTMaterialLike): String
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTBlockPart(private val idPattern: String, override val tagPrefix: HTTagPrefix, provider: HTLangPatternProvider) :
    HTPart,
    HTLangPatternProvider by provider {
    ORE("%s_ore", CommonTagPrefixes.ORE, "%s Ore", "%s鉱石"),
    DEEPSLATE_ORE("deepslate_%s_ore", CommonTagPrefixes.ORE, "Deepslate %s Ore", "深層%s鉱石"),
    NETHER_ORE("nether_%s_ore", CommonTagPrefixes.ORE, "Nether %s Ore", "ネザー%s鉱石"),
    END_ORE("end_%s_ore", CommonTagPrefixes.ORE, "End %s Ore", "エンド%s鉱石"),
    STORAGE_BLOCK("%s_block", CommonTagPrefixes.STORAGE_BLOCK, "Block of %s", "%sブロック"),
    RAW_STORAGE_BLOCK("raw_%s_block", CommonTagPrefixes.RAW_STORAGE_BLOCK, "Block of Raw %s", "%sの原石ブロック"),
    ;

    constructor(idPattern: String, tagPrefix: HTTagPrefix, enPattern: String, jaPattern: String) : this(idPattern, tagPrefix, HTLangPatternProvider(enPattern, jaPattern))

    override fun createName(material: HTMaterialLike): String = idPattern.replace("%s", material.materialName)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTItemPart(private val idPattern: String, override val tagPrefix: HTTagPrefix, provider: HTLangPatternProvider) :
    HTPart,
    HTLangPatternProvider by provider {
    DUST("%s_dust", CommonTagPrefixes.DUST, "%s Dust", "%sの粉"),
    GEAR("%s_gear", CommonTagPrefixes.GEAR, "%s Gear", "%sの歯車"),
    GEM("%s", CommonTagPrefixes.GEM, HTLangPatternProvider.IDENTITY),
    INGOT("%s_ingot", CommonTagPrefixes.INGOT, "%s Ingot", "%sインゴット"),
    NUGGET("%s_nugget", CommonTagPrefixes.NUGGET, "%s Nugget", "%sナゲット"),
    PLATE("%s_plate", CommonTagPrefixes.PLATE, "%s Plate", "%sの板"),
    RAW("raw_%s", CommonTagPrefixes.RAW_MATERIALS, "Raw %s", "%sの原石"),
    TINY("tiny_%s", CommonTagPrefixes.TINY, "Tiny %s", "小さな%s"),
    ;

    constructor(idPattern: String, tagPrefix: HTTagPrefix, enPattern: String, jaPattern: String) : this(idPattern, tagPrefix, HTLangPatternProvider(enPattern, jaPattern))

    override fun createName(material: HTMaterialLike): String = idPattern.replace("%s", material.materialName)
}
