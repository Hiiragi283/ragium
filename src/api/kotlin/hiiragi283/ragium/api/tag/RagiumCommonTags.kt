package hiiragi283.ragium.api.tag

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object RagiumCommonTags {
    //    Blocks    //

    object Blocks {
        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Block> = create("obsidians", "mysterious")

        @JvmField
        val ORES_IN_GROUND_END_STONE: TagKey<Block> = create("ores_in_ground", "end_stone")

        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Block> = create("ores", "deep_scrap")

        @JvmStatic
        private fun create(prefix: String, value: String): TagKey<Block> = Registries.BLOCK.createCommonTag(prefix, value)
    }

    //    Items    //

    object Items {
        @JvmField
        val COAL_COKE: TagKey<Item> = create("coal_coke")

        @JvmField
        val BREAD_SLICES_WHEAT: TagKey<Item> = create("bread_slices/wheat")

        @JvmField
        val FUELS_BIO: TagKey<Item> = create("fuels/bio")

        @JvmField
        val FUELS_BIO_BLOCK: TagKey<Item> = create("fuels/block/bio")

        @JvmField
        val ORES_IN_GROUND_END_STONE: TagKey<Item> = create("ores_in_ground", "end_stone")

        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Item> = create("ores", "deep_scrap")

        @JvmField
        val PLASTIC: TagKey<Item> = create("plastic")

        // Obsidians
        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Item> = create("obsidians", "mysterious")

        @JvmStatic
        private fun create(path: String): TagKey<Item> = Registries.ITEM.createCommonTag(path)

        @JvmStatic
        private fun create(prefix: String, value: String): TagKey<Item> = Registries.ITEM.createCommonTag(prefix, value)
    }
}
