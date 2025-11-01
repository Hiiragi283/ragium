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

sealed interface HTOreVariant :
    HTMaterialVariant,
    HTTranslationProvider {
    companion object {
        @JvmField
        val entries: List<HTOreVariant> = buildList {
            add(Default)
            addAll(Others.entries)
        }
    }

    data object Default : HTOreVariant, HTMaterialVariant.BlockTag {
        override fun variantName(): String = "ore"

        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> "$value Ore"
            HTLanguageType.JA_JP -> "${value}鉱石"
        }

        override val blockCommonTag: TagKey<Block> = Tags.Blocks.ORES

        override fun blockTagKey(path: String): TagKey<Block> = Registries.BLOCK.createCommonTag(RagiumConst.ORES, path)

        override val itemCommonTag: TagKey<Item> = Tags.Items.ORES

        override fun canGenerateTag(): Boolean = true

        override fun itemTagKey(path: String): TagKey<Item> = Registries.ITEM.createCommonTag(RagiumConst.ORES, path)
    }

    enum class Others(private val enPattern: String, private val jaPattern: String) : HTOreVariant {
        DEEP("Deepslate %s Ore", "深層%s鉱石"),
        NETHER("Nether %s Ore", "ネザー%s鉱石"),
        END("End %s Ore", "エンド%s鉱石"),
        ;

        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        }.replace("%s", value)

        override fun variantName(): String = name.lowercase()
    }
}
