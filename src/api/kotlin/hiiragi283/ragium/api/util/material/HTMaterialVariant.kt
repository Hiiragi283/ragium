package hiiragi283.ragium.api.util.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

enum class HTMaterialVariant(
    private val enUsPattern: String,
    private val jaJpPattern: String,
    val tagPrefix: String,
    private val tagNamespace: String = RagiumConst.COMMON,
    val generateTag: Boolean = true,
) : HTVariantKey {
    // Block - Common
    ORE("%s Ore", "%s鉱石", RagiumConst.ORES),
    DEEP_ORE("Deepslate %s Ore", "深層%s鉱石", RagiumConst.ORES, generateTag = false),
    NETHER_ORE("Nether %s Ore", "ネザー%s鉱石", RagiumConst.ORES, generateTag = false),
    END_ORE("End %s Ore", "エンド%s鉱石", RagiumConst.ORES, generateTag = false),

    STORAGE_BLOCK("Block of %s", "%sブロック", RagiumConst.STORAGE_BLOCKS),
    GLASS_BLOCK("%s Glass", "%sガラス", RagiumConst.GLASS_BLOCKS),
    TINTED_GLASS_BLOCK("Tinted %s Glass", "遮光%sガラス", RagiumConst.GLASS_BLOCKS, generateTag = false),

    // Item - Common
    DUST("%s Dust", "%sの粉", RagiumConst.DUSTS),
    GEM("%s", "%s", RagiumConst.GEMS),
    INGOT("%s Ingot", "%sインゴット", RagiumConst.INGOTS),
    NUGGET("%s Nugget", "%sナゲット", RagiumConst.NUGGETS),
    RAW_MATERIAL("Raw %s", "%sの原石", "raw_materials"),

    // Item - Custom
    CIRCUIT("%s Circuit", "%s回路", RagiumConst.CIRCUITS),
    COMPOUND("%s Compound", "%s混合物", RagiumConst.COMPOUNDS, RagiumAPI.MOD_ID),
    FUEL("%s", "%s", RagiumConst.FUELS),
    ;

    companion object {
        @JvmField
        val CREATIVE_TAG_ORDER: List<HTMaterialVariant> = listOf(
            RAW_MATERIAL,
            GEM,
            COMPOUND,
            INGOT,
            NUGGET,
            DUST,
        )
    }

    val blockCommonTag: TagKey<Block> = blockTagKey(ResourceLocation.fromNamespaceAndPath(tagNamespace, tagPrefix))
    val itemCommonTag: TagKey<Item> = itemTagKey(ResourceLocation.fromNamespaceAndPath(tagNamespace, tagPrefix))

    fun blockTagKey(material: HTMaterialType): TagKey<Block> = blockTagKey(material.serializedName)

    fun blockTagKey(path: String): TagKey<Block> = blockTagKey(ResourceLocation.fromNamespaceAndPath(tagNamespace, "$tagPrefix/$path"))

    fun itemTagKey(material: HTMaterialType): TagKey<Item> = itemTagKey(material.serializedName)

    fun itemTagKey(path: String): TagKey<Item> = itemTagKey(ResourceLocation.fromNamespaceAndPath(tagNamespace, "$tagPrefix/$path"))

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
