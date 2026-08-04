package hiiragi283.lib.tag

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * 共通の[TagKey]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTCommonTags {
    data object Blocks {
        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = BlockTags.create(HTConstants.COMMON.toId(*path))
    }

    data object Items {
        // Pearls
        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = ItemTags.create(HTConstants.COMMON.toId(*path))
    }
}
