package hiiragi283.ragium.api.tag

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
     * @since 26.1.0
     */
    data object Items {
        @JvmField
        val SHAPE_PATTERNS: TagKey<Item> = create("shape_patterns")

        @JvmField
        val SOOTY_IRON_TOOL_MATERIALS: TagKey<Item> = create("sooty_iron_tool_materials")

        @JvmStatic
        private fun create(vararg path: String): TagKey<Item> = ItemTags.create(RagiumAPI.id(*path))
    }
}
