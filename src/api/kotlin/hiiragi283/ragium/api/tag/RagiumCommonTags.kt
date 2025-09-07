package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
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
        private fun create(prefix: String, value: String): TagKey<Block> = blockTagKey(commonId("$prefix/$value"))
    }

    //    DamageTypes    //

    object DamageTypes {
        @JvmField
        val IS_SONIC: TagKey<DamageType> = create("is_sonic")

        @JvmStatic
        private fun create(path: String): TagKey<DamageType> = TagKey.create(Registries.DAMAGE_TYPE, RagiumAPI.id(path))
    }

    //    Fluids    //

    object Fluids {
        @JvmField
        val CHOCOLATES: TagKey<Fluid> = create("chocolates")

        @JvmField
        val MEAT: TagKey<Fluid> = create("meat")

        @JvmStatic
        private fun create(path: String): TagKey<Fluid> = fluidTagKey(commonId(path))
    }

    //    Items    //

    object Items {
        @JvmField
        val FUELS_BIO: TagKey<Item> = create("fuels/bio")

        @JvmField
        val FUELS_BIO_BLOCK: TagKey<Item> = create("fuels/block/bio")

        @JvmField
        val ORES_IN_GROUND_END_STONE: TagKey<Item> = create("ores_in_ground", "end_stone")

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
        private fun create(path: String): TagKey<Item> = itemTagKey(commonId(path))

        @JvmStatic
        private fun create(prefix: String, value: String): TagKey<Item> = itemTagKey(commonId("$prefix/$value"))
    }
}
