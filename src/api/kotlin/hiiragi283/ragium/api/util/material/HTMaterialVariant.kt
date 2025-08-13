package hiiragi283.ragium.api.util.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

enum class HTMaterialVariant(
    private val enUsPattern: String,
    private val jaJpPattern: String,
    val tagPrefix: String,
    val commonTag: TagKey<Item>,
    private val tagNamespace: String = RagiumConst.COMMON,
) : HTVariantKey {
    // Common
    DUST("%s Dust", "%sの粉", RagiumConst.DUSTS, Tags.Items.DUSTS),
    GEM("%s", "%s", RagiumConst.GEMS, Tags.Items.GEMS),
    INGOT("%s Ingot", "%sインゴット", RagiumConst.INGOTS, Tags.Items.INGOTS),
    NUGGET("%s Nugget", "%sナゲット", RagiumConst.NUGGETS, Tags.Items.NUGGETS),
    ORE("%s Ore", "%s鉱石", RagiumConst.ORES, Tags.Items.ORES),
    RAW_MATERIAL("Raw %s", "%sの原石", "raw_materials", Tags.Items.RAW_MATERIALS),
    STORAGE_BLOCK("Block of %s", "%sブロック", RagiumConst.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS),

    // Custom
    CIRCUIT("%s Circuit", "%s回路", RagiumConst.CIRCUITS, RagiumCommonTags.Items.CIRCUITS),
    COMPOUND("%s Compound", "%s混合物", RagiumConst.COMPOUNDS, RagiumModTags.Items.COMPOUNDS, RagiumAPI.MOD_ID),
    GLASS_BLOCK("%s Glass Block", "%sガラス", RagiumConst.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS),
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
