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

object HTRawStorageMaterialVariant : HTMaterialVariant.BlockTag, HTTranslationProvider {
    override val blockCommonTag: TagKey<Block> = Tags.Blocks.STORAGE_BLOCKS

    override fun blockTagKey(path: String): TagKey<Block> = Registries.BLOCK.createCommonTag("${RagiumConst.STORAGE_BLOCKS}/raw_$path")

    override val itemCommonTag: TagKey<Item> = Tags.Items.STORAGE_BLOCKS

    override fun canGenerateTag(): Boolean = false

    override fun itemTagKey(path: String): TagKey<Item> = Registries.ITEM.createCommonTag("${RagiumConst.STORAGE_BLOCKS}/raw_$path")

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "Block of Raw $value"
        HTLanguageType.JA_JP -> "${value}の原石ブロック"
    }

    override fun variantName(): String = "raw_storage_block"
}
