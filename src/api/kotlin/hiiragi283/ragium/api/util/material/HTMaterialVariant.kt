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
import net.minecraft.world.item.crafting.Ingredient
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
    PLATE("%s Plate", "%s板", "plates"),
    RAW_MATERIAL("Raw %s", "%sの原石", "raw_materials"),
    ROD("%s Rod", "%s棒", "rods"),

    // Item - Custom
    CIRCUIT("%s Circuit", "%s回路", RagiumConst.CIRCUITS),
    COIL("%s Coil", "%sコイル", "coils", RagiumAPI.MOD_ID),
    COMPOUND("%s Compound", "%s混合物", "compounds", RagiumAPI.MOD_ID),
    FUEL("%s", "%s", RagiumConst.FUELS),
    ;

    companion object {
        @JvmField
        val CREATIVE_TAG_ORDER: List<HTMaterialVariant> = listOf(
            RAW_MATERIAL,
            GEM,
            COMPOUND,
            INGOT,
            COIL,
            NUGGET,
            DUST,
        )
    }

    private fun id(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)

    val blockCommonTag: TagKey<Block> = blockTagKey(id(tagNamespace, tagPrefix))
    val itemCommonTag: TagKey<Item> = itemTagKey(id(tagNamespace, tagPrefix))

    fun blockTagKey(material: HTMaterialType): TagKey<Block> = blockTagKey(material.serializedName)

    fun blockTagKey(path: String): TagKey<Block> = blockTagKey(id(tagNamespace, "$tagPrefix/$path"))

    fun itemTagKey(material: HTMaterialType): TagKey<Item> = itemTagKey(material.serializedName)

    fun itemTagKey(path: String): TagKey<Item> = itemTagKey(id(tagNamespace, "$tagPrefix/$path"))

    fun toIngredient(material: HTMaterialType): Ingredient = Ingredient.of(itemTagKey(material))

    fun toIngredient(path: String): Ingredient = Ingredient.of(itemTagKey(path))

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
