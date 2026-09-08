package hiiragi283.ragium.api.tag

import hiiragi283.lib.tag.BlockItemTag
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * Ragiumで使用される[TagKey]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumTags {
    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    data object BlockItem {
        @JvmField
        val QUARTZ_BLOCKS: BlockItemTag = create("quartz_blocks")

        @JvmStatic
        private fun create(vararg path: String): BlockItemTag = BlockItemTag(RagiumAPI.id(*path))
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    data object Items {
        @JvmField
        val SOOTY_IRON_TOOL_MATERIALS: TagKey<Item> = create("sooty_iron_tool_materials")

        @JvmStatic
        private fun create(vararg path: String): TagKey<Item> = ItemTags.create(RagiumAPI.id(*path))
    }
}
