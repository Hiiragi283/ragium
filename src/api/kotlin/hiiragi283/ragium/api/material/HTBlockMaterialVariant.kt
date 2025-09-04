package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

enum class HTBlockMaterialVariant(private val enUsPattern: String, private val jaJpPattern: String, private val tagPrefix: String?) :
    HTMaterialVariant.BlockTag {
    ORE("%s Ore", "%s鉱石", RagiumConst.ORES),
    DEEP_ORE("Deepslate %s Ore", "深層%s鉱石", null),
    NETHER_ORE("Nether %s Ore", "ネザー%s鉱石", null),
    END_ORE("End %s Ore", "エンド%s鉱石", null),

    STORAGE_BLOCK("Block of %s", "%sブロック", RagiumConst.STORAGE_BLOCKS),
    GLASS_BLOCK("%s Glass", "%sガラス", RagiumConst.GLASS_BLOCKS),
    TINTED_GLASS_BLOCK("Tinted %s Glass", "遮光%sガラス", null),
    ;

    override val blockCommonTag: TagKey<Block>? = tagPrefix?.let { blockTagKey(commonId(it)) }
    override val itemCommonTag: TagKey<Item>? = tagPrefix?.let { itemTagKey(commonId(it)) }

    override fun canGenerateTag(): Boolean = tagPrefix != null

    private fun checkTagPrefix(): String = checkNotNull(tagPrefix) { "Tag creation is not allowed for $serializedName!" }

    override fun blockTagKey(path: String): TagKey<Block> = blockTagKey(commonId("${checkTagPrefix()}/$path"))

    override fun itemTagKey(path: String): TagKey<Item> = itemTagKey(commonId("${checkTagPrefix()}/$path"))

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
