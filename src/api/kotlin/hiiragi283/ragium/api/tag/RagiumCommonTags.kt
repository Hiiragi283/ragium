package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.util.RagiumConst
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
        val GLASS_BLOCKS_OBSIDIAN: TagKey<Block> = commonTag(RagiumConst.GLASS_BLOCKS, RagiumConst.OBSIDIAN)

        @JvmField
        val GLASS_BLOCKS_QUARTZ: TagKey<Block> = commonTag(RagiumConst.GLASS_BLOCKS, RagiumConst.QUARTZ)

        @JvmField
        val GLASS_BLOCKS_SOUL: TagKey<Block> = commonTag(RagiumConst.GLASS_BLOCKS, RagiumConst.SOUL)

        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Block> = commonTag("obsidians", "mysterious")

        @JvmField
        val ORES_RAGI_CRYSTAL: TagKey<Block> = commonTag(RagiumConst.ORES, RagiumConst.RAGI_CRYSTAL)

        @JvmField
        val ORES_RAGINITE: TagKey<Block> = commonTag(RagiumConst.ORES, RagiumConst.RAGINITE)

        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Block> = commonTag(RagiumConst.ORES, "deep_scrap")

        @JvmStatic
        private fun commonTag(path: String): TagKey<Block> = blockTagKey(commonId(path))

        @JvmStatic
        private fun commonTag(prefix: String, value: String): TagKey<Block> = blockTagKey(commonId(prefix, value))
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
        val CREOSOTE: TagKey<Fluid> = fluidTagKey(commonId("creosote"))

        @JvmField
        val CHOCOLATES: TagKey<Fluid> = fluidTagKey(commonId("chocolates"))

        @JvmField
        val MEAT: TagKey<Fluid> = fluidTagKey(commonId("meat"))
    }

    //    Items    //

    object Items {
        @JvmField
        val COAL_COKE: TagKey<Item> = commonTag("coal_coke")

        @JvmField
        val FUELS_BIO: TagKey<Item> = commonTag("fuels/bio")

        @JvmField
        val FUELS_BIO_BLOCK: TagKey<Item> = commonTag("fuels/block/bio")

        @JvmField
        val PAPER: TagKey<Item> = commonTag("paper")

        @JvmField
        val PLASTICS: TagKey<Item> = commonTag("plastics")

        @JvmField
        val SILICON: TagKey<Item> = commonTag("silicon")

        // Circuits
        @JvmField
        val CIRCUITS: TagKey<Item> = commonTag(RagiumConst.CIRCUITS)

        @JvmField
        val CIRCUITS_BASIC: TagKey<Item> = commonTag(RagiumConst.CIRCUITS, "basic")

        @JvmField
        val CIRCUITS_ADVANCED: TagKey<Item> = commonTag(RagiumConst.CIRCUITS, "advanced")

        @JvmField
        val CIRCUITS_ELITE: TagKey<Item> = commonTag(RagiumConst.CIRCUITS, "elite")

        @JvmField
        val CIRCUITS_ULTIMATE: TagKey<Item> = commonTag(RagiumConst.CIRCUITS, "ultimate")

        // Crops
        @JvmField
        val CROPS_WARPED_WART: TagKey<Item> = commonTag("crops", "warped_wart")

        // Dusts
        private const val DUSTS: String = RagiumConst.DUSTS

        @JvmField
        val DUSTS_ASH: TagKey<Item> = commonTag(DUSTS, "ash")

        @JvmField
        val DUSTS_OBSIDIAN: TagKey<Item> = commonTag(DUSTS, RagiumConst.OBSIDIAN)

        @JvmField
        val DUSTS_RAGINITE: TagKey<Item> = commonTag(DUSTS, RagiumConst.RAGINITE)

        @JvmField
        val DUSTS_CINNABAR: TagKey<Item> = commonTag(DUSTS, "cinnabar")

        @JvmField
        val DUSTS_SALTPETER: TagKey<Item> = commonTag(DUSTS, "saltpeter")

        @JvmField
        val DUSTS_SULFUR: TagKey<Item> = commonTag(DUSTS, "sulfur")

        @JvmField
        val DUSTS_MEAT: TagKey<Item> = commonTag(DUSTS, "meat")

        @JvmField
        val DUSTS_WOOD: TagKey<Item> = commonTag(DUSTS, "wood")

        // Foods
        @JvmField
        val FOODS_CHOCOLATE: TagKey<Item> = commonTag("foods", RagiumConst.CHOCOLATE)

        // Foods - Cherry
        @JvmField
        val FOODS_CHERRY: TagKey<Item> = commonTag("foods", "cherry")

        @JvmField
        val FOODS_RAGI_CHERRY: TagKey<Item> = commonTag("foods", "ragi_cherry")

        // Foods - Jam
        @JvmField
        val FOODS_JAMS: TagKey<Item> = commonTag("foods", "jams")

        @JvmField
        val JAMS_RAGI_CHERRY: TagKey<Item> = commonTag("jams", "ragi_cherry")

        // Glasses
        @JvmField
        val GLASS_BLOCKS_OBSIDIAN: TagKey<Item> = commonTag(RagiumConst.GLASS_BLOCKS, RagiumConst.OBSIDIAN)

        @JvmField
        val GLASS_BLOCKS_QUARTZ: TagKey<Item> = commonTag(RagiumConst.GLASS_BLOCKS, RagiumConst.QUARTZ)

        @JvmField
        val GLASS_BLOCKS_SOUL: TagKey<Item> = commonTag(RagiumConst.GLASS_BLOCKS, RagiumConst.SOUL)

        // Gems
        private const val GEMS: String = RagiumConst.GEMS

        @JvmField
        val GEMS_RAGI_CRYSTAL: TagKey<Item> = commonTag(GEMS, RagiumConst.RAGI_CRYSTAL)

        @JvmField
        val GEMS_AZURE: TagKey<Item> = commonTag(GEMS, "azure")

        @JvmField
        val GEMS_CRIMSON_CRYSTAL: TagKey<Item> = commonTag(GEMS, RagiumConst.CRIMSON_CRYSTAL)

        @JvmField
        val GEMS_WARPED_CRYSTAL: TagKey<Item> = commonTag(GEMS, RagiumConst.WARPED_CRYSTAL)

        @JvmField
        val GEMS_ELDRITCH_PEARL: TagKey<Item> = commonTag(GEMS, RagiumConst.ELDRITCH_PEARL)

        // Ingots
        private const val INGOTS: String = RagiumConst.INGOTS

        @JvmField
        val INGOTS_RAGI_ALLOY: TagKey<Item> = commonTag(INGOTS, RagiumConst.RAGI_ALLOY)

        @JvmField
        val INGOTS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag(INGOTS, RagiumConst.ADVANCED_RAGI_ALLOY)

        @JvmField
        val INGOTS_AZURE_STEEL: TagKey<Item> = commonTag(INGOTS, RagiumConst.AZURE_STEEL)

        @JvmField
        val INGOTS_DEEP_STEEL: TagKey<Item> = commonTag(INGOTS, RagiumConst.DEEP_STEEL)

        @JvmField
        val INGOTS_CHOCOLATE: TagKey<Item> = commonTag(INGOTS, RagiumConst.CHOCOLATE)

        @JvmField
        val INGOTS_MEAT: TagKey<Item> = commonTag(INGOTS, RagiumConst.MEAT)

        @JvmField
        val INGOTS_COOKED_MEAT: TagKey<Item> = commonTag(INGOTS, RagiumConst.COOKED_MEAT)

        @JvmField
        val BEACON_PAYMENTS: Array<TagKey<Item>> = arrayOf(
            // gems
            GEMS_RAGI_CRYSTAL,
            GEMS_AZURE,
            GEMS_CRIMSON_CRYSTAL,
            GEMS_WARPED_CRYSTAL,
            GEMS_ELDRITCH_PEARL,
            // ingots
            INGOTS_RAGI_ALLOY,
            INGOTS_ADVANCED_RAGI_ALLOY,
            INGOTS_AZURE_STEEL,
            INGOTS_DEEP_STEEL,
        )

        // Nuggets
        private const val NUGGETS: String = RagiumConst.NUGGETS

        @JvmField
        val NUGGETS_RAGI_ALLOY: TagKey<Item> = commonTag(NUGGETS, RagiumConst.RAGI_ALLOY)

        @JvmField
        val NUGGETS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag(NUGGETS, RagiumConst.ADVANCED_RAGI_ALLOY)

        @JvmField
        val NUGGETS_AZURE_STEEL: TagKey<Item> = commonTag(NUGGETS, RagiumConst.AZURE_STEEL)

        // Obsidians
        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Item> = commonTag("obsidians", "mysterious")

        // Ores
        private const val ORES: String = RagiumConst.ORES

        @JvmField
        val ORES_RAGINITE: TagKey<Item> = commonTag(ORES, RagiumConst.RAGINITE)

        @JvmField
        val ORES_RAGI_CRYSTAL: TagKey<Item> = commonTag(ORES, RagiumConst.RAGI_CRYSTAL)

        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Item> = commonTag(ORES, "deep_scrap")

        // Plates
        @JvmField
        val PLATES: TagKey<Item> = commonTag(RagiumConst.PLATES)

        @JvmField
        val PLATES_PLASTIC: TagKey<Item> = commonTag(RagiumConst.PLATES, "plastic")

        // Storage Blocks
        private const val STORAGE_BLOCKS: String = RagiumConst.STORAGE_BLOCKS

        @JvmField
        val STORAGE_BLOCKS_RAGI_CRYSTAL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.RAGI_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_CRIMSON_CRYSTAL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.CRIMSON_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_WARPED_CRYSTAL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.WARPED_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_ELDRITCH_PEARL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.ELDRITCH_PEARL)

        @JvmField
        val STORAGE_BLOCKS_RAGI_ALLOY: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.RAGI_ALLOY)

        @JvmField
        val STORAGE_BLOCKS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.ADVANCED_RAGI_ALLOY)

        @JvmField
        val STORAGE_BLOCKS_AZURE_STEEL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.AZURE_STEEL)

        @JvmField
        val STORAGE_BLOCKS_DEEP_STEEL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.DEEP_STEEL)

        @JvmField
        val STORAGE_BLOCKS_CHOCOLATE: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.CHOCOLATE)

        @JvmField
        val STORAGE_BLOCKS_MEAT: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.MEAT)

        @JvmField
        val STORAGE_BLOCKS_COOKED_MEAT: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConst.COOKED_MEAT)

        // Tools
        @JvmField
        val TOOLS_FORGE_HAMMER: TagKey<Item> = commonTag("tools", "forge_hammer")

        // Mekanism Integration
        @JvmField
        val ENRICHED_RAGINITE: TagKey<Item> = commonTag(RagiumConst.ENRICHED, RagiumConst.RAGINITE)

        @JvmField
        val ENRICHED_AZURE: TagKey<Item> = commonTag(RagiumConst.ENRICHED, "azure")

        @JvmStatic
        private fun commonTag(path: String): TagKey<Item> = itemTagKey(commonId(path))

        @JvmStatic
        private fun commonTag(prefix: String, value: String): TagKey<Item> = itemTagKey(commonId(prefix, value))
    }
}
