package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

object RagiumCommonTags {
    //    Blocks    //

    object Blocks {
        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Block> = create("obsidians", "mysterious")

        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Block> = create(RagiumConst.ORES, "deep_scrap")

        @JvmField
        val ORES_IN_GROUND_END_STONE: TagKey<Block> = create("ores_in_ground", "end_stone")

        @JvmStatic
        private fun create(prefix: String, value: String): TagKey<Block> = Registries.BLOCK.createCommonTag(prefix, value)
    }

    //    Fluids    //

    object Fluids {
        @JvmField
        val CHOCOLATES: TagKey<Fluid> = create("chocolates")

        @JvmField
        val MEAT: TagKey<Fluid> = create("meat")

        @JvmStatic
        private fun create(path: String): TagKey<Fluid> = Registries.FLUID.createCommonTag(path)
    }

    //    Items    //

    object Items {
        @JvmField
        val COAL_COKE: TagKey<Item> = create(RagiumConst.COAL_COKE)

        @JvmField
        val FUELS_BIO: TagKey<Item> = create("fuels/bio")

        @JvmField
        val FUELS_BIO_BLOCK: TagKey<Item> = create("fuels/block/bio")

        @JvmField
        val ORES_IN_GROUND_END_STONE: TagKey<Item> = create("ores_in_ground", "end_stone")

        @JvmField
        val PLASTIC: TagKey<Item> = create("plastic")

        // Crops
        @JvmField
        val CROPS_WARPED_WART: TagKey<Item> = create("crops", "warped_wart")

        // Foods
        @JvmField
        val FOODS_APPLE: TagKey<Item> = create("foods", "apple")

        @JvmField
        val FOODS_CHOCOLATE: TagKey<Item> = create("foods", RagiumConst.CHOCOLATE)

        // Foods - Cherry
        @JvmField
        val FOODS_CHERRY: TagKey<Item> = create("foods", "cherry")

        @JvmField
        val FOODS_RAGI_CHERRY: TagKey<Item> = create("foods", RagiumConst.RAGI_CHERRY)

        // Foods - Jam
        @JvmField
        val JAMS: TagKey<Item> = create("jams")

        @JvmField
        val JAMS_RAGI_CHERRY: TagKey<Item> = create("jams", RagiumConst.RAGI_CHERRY)

        // Obsidians
        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Item> = create("obsidians", "mysterious")

        // Ores
        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Item> = create(RagiumConst.ORES, "deep_scrap")

        @JvmStatic
        private fun create(path: String): TagKey<Item> = Registries.ITEM.createCommonTag(path)

        @JvmStatic
        private fun create(prefix: String, value: String): TagKey<Item> = Registries.ITEM.createCommonTag(prefix, value)
    }
}
