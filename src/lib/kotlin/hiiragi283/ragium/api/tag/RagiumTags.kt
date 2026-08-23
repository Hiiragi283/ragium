package hiiragi283.ragium.api.tag

import hiiragi283.lib.tag.RawTagKey
import hiiragi283.lib.tag.createTagKey
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

data object RagiumTags {

    data object Items {
        @JvmField
        val CROPS_WARPED_WART: TagKey<Item> = common("crops", "warped_wart")

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

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = RawTagKey.common(*path).create(Registries.ITEM)

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(RagiumAPI.id(*path))
    }
}
