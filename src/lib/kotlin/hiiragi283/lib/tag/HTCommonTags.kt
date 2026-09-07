package hiiragi283.lib.tag

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * 共通の[TagKey]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTCommonTags {
    data object Blocks {
        @JvmStatic
        private fun create(vararg path: String): TagKey<Block> = BlockTags.create(HTConstants.COMMON.toId(*path))
    }

    data object Fluids {
        @JvmStatic
        private fun create(vararg path: String): TagKey<Fluid> = FluidTags.create(HTConstants.COMMON.toId(*path))
    }

    data object Items {
        @JvmField
        val FOODS_DOUGH_WHEAT: TagKey<Item> = create("foods", "dough", "wheat")

        @JvmField
        val FLOURS: TagKey<Item> = create("flours")

        @JvmField
        val FLOURS_WHEAT: TagKey<Item> = create("flours", "wheat")

        @JvmField
        val PAPER: TagKey<Item> = create("paper")

        @JvmField
        val PLASTICS: TagKey<Item> = create("plastics")

        @JvmField
        val SILICON: TagKey<Item> = create("silicon")

        @JvmField
        val STICKY_BALLS: TagKey<Item> = create("sticky_balls")

        @JvmStatic
        private fun create(vararg path: String): TagKey<Item> = ItemTags.create(HTConstants.COMMON.toId(*path))
    }
}
