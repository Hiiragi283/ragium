package hiiragi283.ragium.api.tag

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.createTagKey
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
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
        // Common
        @JvmField
        val FOODS_DOUGH_WHEAT: TagKey<Item> = common("foods", "dough", "wheat")

        @JvmField
        val FLOURS: TagKey<Item> = common("flours")

        @JvmField
        val FLOURS_WHEAT: TagKey<Item> = common("flours", "wheat")

        @JvmField
        val PAPER: TagKey<Item> = common("paper")

        @JvmField
        val PLASTICS: TagKey<Item> = common("plastics")

        @JvmField
        val SILICON: TagKey<Item> = common("silicon")

        @JvmField
        val STICKY_BALLS: TagKey<Item> = common("sticky_balls")

        // Modded
        @JvmField
        val SHAPE_PATTERNS: TagKey<Item> = mod("shape_patterns")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = ItemTags.create(HTConstants.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(RagiumAPI.id(*path))
    }
}
