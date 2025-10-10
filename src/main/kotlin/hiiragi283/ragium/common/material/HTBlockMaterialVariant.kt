package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.tag.createCommonTag
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

enum class HTBlockMaterialVariant(private val enPattern: String, private val jaPattern: String, private val tagPrefix: String?) :
    HTMaterialVariant.BlockTag {
    ORE("%s Ore", "%s鉱石", RagiumConst.ORES),
    DEEP_ORE("Deepslate %s Ore", "深層%s鉱石", null),
    NETHER_ORE("Nether %s Ore", "ネザー%s鉱石", null),
    END_ORE("End %s Ore", "エンド%s鉱石", null),

    STORAGE_BLOCK("Block of %s", "%sブロック", RagiumConst.STORAGE_BLOCKS),
    GLASS_BLOCK("%s Glass", "%sガラス", RagiumConst.GLASS_BLOCKS),
    TINTED_GLASS_BLOCK("Tinted %s Glass", "遮光%sガラス", null),
    ;

    override val blockCommonTag: TagKey<Block>? = tagPrefix?.let(Registries.BLOCK::createCommonTag)
    override val itemCommonTag: TagKey<Item>? = tagPrefix?.let(Registries.ITEM::createCommonTag)

    override fun canGenerateTag(): Boolean = tagPrefix != null

    private fun checkTagPrefix(): String = checkNotNull(tagPrefix) { "Tag creation is not allowed for ${variantName()}!" }

    override fun blockTagKey(path: String): TagKey<Block> = Registries.BLOCK.createCommonTag(checkTagPrefix(), path)

    override fun itemTagKey(path: String): TagKey<Item> = Registries.ITEM.createCommonTag(checkTagPrefix(), path)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
