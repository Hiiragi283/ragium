package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslationProvider
import hiiragi283.ragium.api.tag.createCommonTag
import hiiragi283.ragium.api.variant.HTMaterialVariant
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

enum class HTGlassVariant(
    private val enPattern: String,
    private val jaPattern: String,
    override val blockCommonTag: TagKey<Block>,
    override val itemCommonTag: TagKey<Item>,
    private val tagPrefix: String,
) : HTMaterialVariant.BlockTag,
    HTTranslationProvider {
    COLORLESS("%s Glass", "%sガラス", Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS, RagiumConst.GLASS_BLOCKS),
    TINTED("Tinted %s Glass", "遮光%sガラス", Tags.Blocks.GLASS_BLOCKS_TINTED, Tags.Items.GLASS_BLOCKS_TINTED, RagiumConst.GLASS_BLOCKS_TINTED),
    ;

    override fun blockTagKey(path: String): TagKey<Block> = Registries.BLOCK.createCommonTag(tagPrefix, path)

    override fun canGenerateTag(): Boolean = true

    override fun itemTagKey(path: String): TagKey<Item> = Registries.ITEM.createCommonTag(tagPrefix, path)

    override fun variantName(): String = name.lowercase()

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)
}
