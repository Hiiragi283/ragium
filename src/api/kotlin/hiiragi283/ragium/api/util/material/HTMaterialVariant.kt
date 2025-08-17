package hiiragi283.ragium.api.util.material

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
    private val tagPrefix: String?,
    private val tagNamespace: String = RagiumConst.COMMON,
) : HTVariantKey {
    // Block - Common
    ORE("%s Ore", "%s鉱石", RagiumConst.ORES),
    DEEP_ORE("Deepslate %s Ore", "深層%s鉱石", null),
    NETHER_ORE("Nether %s Ore", "ネザー%s鉱石", null),
    END_ORE("End %s Ore", "エンド%s鉱石", null),

    STORAGE_BLOCK("Block of %s", "%sブロック", RagiumConst.STORAGE_BLOCKS),
    GLASS_BLOCK("%s Glass", "%sガラス", RagiumConst.GLASS_BLOCKS),
    TINTED_GLASS_BLOCK("Tinted %s Glass", "遮光%sガラス", null),

    // Item - Common
    DUST("%s Dust", "%sの粉", RagiumConst.DUSTS),
    GEM("%s", "%s", RagiumConst.GEMS),
    GEAR("%s Gear", "%sの歯車", "gears"),
    INGOT("%s Ingot", "%sインゴット", RagiumConst.INGOTS),
    NUGGET("%s Nugget", "%sナゲット", RagiumConst.NUGGETS),
    PLATE("%s Plate", "%s板", "plates"),
    RAW_MATERIAL("Raw %s", "%sの原石", "raw_materials"),
    ROD("%s Rod", "%s棒", "rods"),

    // Item - Custom
    CIRCUIT("%s Circuit", "%s回路", RagiumConst.CIRCUITS),
    COIL("%s Coil", "%sコイル", null),
    COMPONENT("%s Component", "%s構造体", null),
    COMPOUND("%s Compound", "%s混合物", null),
    FUEL("%s", "%s", RagiumConst.FUELS),
    ;

    companion object {
        @JvmField
        val MATERIAL_TAB_ORDER: List<HTMaterialVariant> = listOf(
            RAW_MATERIAL,
            GEM,
            COMPOUND,
            INGOT,
            NUGGET,
            DUST,
        )

        @JvmField
        val CIRCUIT_TAB_ORDER: List<HTMaterialVariant> = listOf(
            COIL,
            CIRCUIT,
            COMPONENT,
        )
    }

    private fun id(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)

    val blockCommonTag: TagKey<Block>? = tagPrefix?.let { blockTagKey(id(tagNamespace, it)) }
    val itemCommonTag: TagKey<Item>? = tagPrefix?.let { itemTagKey(id(tagNamespace, it)) }

    fun canGenerateTag(): Boolean = tagPrefix != null

    private fun checkTagPrefix(): String = checkNotNull(tagPrefix) { "Tag creation is not allowed for $serializedName!" }

    fun blockTagKey(material: HTMaterialType): TagKey<Block> = blockTagKey(material.serializedName)

    fun blockTagKey(path: String): TagKey<Block> = blockTagKey(id(tagNamespace, "${checkTagPrefix()}/$path"))

    fun itemTagKey(material: HTMaterialType): TagKey<Item> = itemTagKey(material.serializedName)

    fun itemTagKey(path: String): TagKey<Item> = itemTagKey(id(tagNamespace, "${checkTagPrefix()}/$path"))

    fun toIngredient(material: HTMaterialType): Ingredient = Ingredient.of(itemTagKey(material))

    fun toIngredient(path: String): Ingredient = Ingredient.of(itemTagKey(path))

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
