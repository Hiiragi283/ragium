package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.util.RagiumConstantValues
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
        val GLASS_BLOCKS_OBSIDIAN: TagKey<Block> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.OBSIDIAN)

        @JvmField
        val GLASS_BLOCKS_QUARTZ: TagKey<Block> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.QUARTZ)

        @JvmField
        val GLASS_BLOCKS_SOUL: TagKey<Block> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.SOUL)

        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Block> = commonTag("obsidians", "mysterious")

        @JvmField
        val ORES_RAGI_CRYSTAL: TagKey<Block> = commonTag(RagiumConstantValues.ORES, RagiumConstantValues.RAGI_CRYSTAL)

        @JvmField
        val ORES_RAGINITE: TagKey<Block> = commonTag(RagiumConstantValues.ORES, RagiumConstantValues.RAGINITE)

        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Block> = commonTag(RagiumConstantValues.ORES, "deep_scrap")

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
        val CHOCOLATES: TagKey<Fluid> = fluidTagKey(commonId("chocolates"))

        @JvmField
        val MEAT: TagKey<Fluid> = fluidTagKey(commonId("meat"))
    }

    //    Items    //

    object Items {
        @JvmField
        val COAL_COKES: TagKey<Item> = commonTag("coal_cokes")

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
        val CIRCUITS: TagKey<Item> = commonTag(RagiumConstantValues.CIRCUITS)

        @JvmField
        val CIRCUITS_BASIC: TagKey<Item> = commonTag(RagiumConstantValues.CIRCUITS, "basic")

        @JvmField
        val CIRCUITS_ADVANCED: TagKey<Item> = commonTag(RagiumConstantValues.CIRCUITS, "advanced")

        @JvmField
        val CIRCUITS_ELITE: TagKey<Item> = commonTag(RagiumConstantValues.CIRCUITS, "elite")

        @JvmField
        val CIRCUITS_ULTIMATE: TagKey<Item> = commonTag(RagiumConstantValues.CIRCUITS, "ultimate")

        // Crops
        @JvmField
        val CROPS_WARPED_WART: TagKey<Item> = commonTag("crops", "warped_wart")

        // Dusts
        private const val DUSTS: String = RagiumConstantValues.DUSTS

        @JvmField
        val DUSTS_ASH: TagKey<Item> = commonTag(DUSTS, "ash")

        @JvmField
        val DUSTS_OBSIDIAN: TagKey<Item> = commonTag(DUSTS, RagiumConstantValues.OBSIDIAN)

        @JvmField
        val DUSTS_RAGINITE: TagKey<Item> = commonTag(DUSTS, RagiumConstantValues.RAGINITE)

        @JvmField
        val DUSTS_CINNABAR: TagKey<Item> = commonTag(DUSTS, "cinnabar")

        @JvmField
        val DUSTS_QUARTZ: TagKey<Item> = commonTag(DUSTS, "quartz")

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
        val FOODS_CHOCOLATE: TagKey<Item> = commonTag("foods", RagiumConstantValues.CHOCOLATE)

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
        val GLASS_BLOCKS_OBSIDIAN: TagKey<Item> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.OBSIDIAN)

        @JvmField
        val GLASS_BLOCKS_QUARTZ: TagKey<Item> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.QUARTZ)

        @JvmField
        val GLASS_BLOCKS_SOUL: TagKey<Item> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.SOUL)

        // Gems
        private const val GEMS: String = RagiumConstantValues.GEMS

        @JvmField
        val GEMS_RAGI_CRYSTAL: TagKey<Item> = commonTag(GEMS, RagiumConstantValues.RAGI_CRYSTAL)

        @JvmField
        val GEMS_AZURE: TagKey<Item> = commonTag(GEMS, "azure")

        @JvmField
        val GEMS_CRIMSON_CRYSTAL: TagKey<Item> = commonTag(GEMS, RagiumConstantValues.CRIMSON_CRYSTAL)

        @JvmField
        val GEMS_WARPED_CRYSTAL: TagKey<Item> = commonTag(GEMS, RagiumConstantValues.WARPED_CRYSTAL)

        @JvmField
        val GEMS_ELDRITCH_PEARL: TagKey<Item> = commonTag(GEMS, RagiumConstantValues.ELDRITCH_PEARL)

        // Ingots
        private const val INGOTS: String = RagiumConstantValues.INGOTS

        @JvmField
        val INGOTS_RAGI_ALLOY: TagKey<Item> = commonTag(INGOTS, RagiumConstantValues.RAGI_ALLOY)

        @JvmField
        val INGOTS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag(INGOTS, RagiumConstantValues.ADVANCED_RAGI_ALLOY)

        @JvmField
        val INGOTS_AZURE_STEEL: TagKey<Item> = commonTag(INGOTS, RagiumConstantValues.AZURE_STEEL)

        @JvmField
        val INGOTS_DEEP_STEEL: TagKey<Item> = commonTag(INGOTS, RagiumConstantValues.DEEP_STEEL)

        @JvmField
        val INGOTS_CHOCOLATE: TagKey<Item> = commonTag(INGOTS, RagiumConstantValues.CHOCOLATE)

        @JvmField
        val INGOTS_MEAT: TagKey<Item> = commonTag(INGOTS, RagiumConstantValues.MEAT)

        @JvmField
        val INGOTS_COOKED_MEAT: TagKey<Item> = commonTag(INGOTS, RagiumConstantValues.COOKED_MEAT)

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
        private const val NUGGETS: String = RagiumConstantValues.NUGGETS

        @JvmField
        val NUGGETS_RAGI_ALLOY: TagKey<Item> = commonTag(NUGGETS, RagiumConstantValues.RAGI_ALLOY)

        @JvmField
        val NUGGETS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag(NUGGETS, RagiumConstantValues.ADVANCED_RAGI_ALLOY)

        @JvmField
        val NUGGETS_AZURE_STEEL: TagKey<Item> = commonTag(NUGGETS, RagiumConstantValues.AZURE_STEEL)

        // Obsidians
        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Item> = commonTag("obsidians", "mysterious")

        // Ores
        private const val ORES: String = RagiumConstantValues.ORES

        @JvmField
        val ORES_RAGINITE: TagKey<Item> = commonTag(ORES, RagiumConstantValues.RAGINITE)

        @JvmField
        val ORES_RAGI_CRYSTAL: TagKey<Item> = commonTag(ORES, RagiumConstantValues.RAGI_CRYSTAL)

        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Item> = commonTag(ORES, "deep_scrap")

        // Plates
        @JvmField
        val PLATES: TagKey<Item> = commonTag(RagiumConstantValues.PLATES)

        @JvmField
        val PLATES_PLASTIC: TagKey<Item> = commonTag(RagiumConstantValues.PLATES, "plastic")

        // Storage Blocks
        private const val STORAGE_BLOCKS: String = RagiumConstantValues.STORAGE_BLOCKS

        @JvmField
        val STORAGE_BLOCKS_RAGI_CRYSTAL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.RAGI_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_CRIMSON_CRYSTAL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.CRIMSON_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_WARPED_CRYSTAL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.WARPED_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_ELDRITCH_PEARL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.ELDRITCH_PEARL)

        @JvmField
        val STORAGE_BLOCKS_RAGI_ALLOY: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.RAGI_ALLOY)

        @JvmField
        val STORAGE_BLOCKS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.ADVANCED_RAGI_ALLOY)

        @JvmField
        val STORAGE_BLOCKS_AZURE_STEEL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.AZURE_STEEL)

        @JvmField
        val STORAGE_BLOCKS_DEEP_STEEL: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.DEEP_STEEL)

        @JvmField
        val STORAGE_BLOCKS_CHOCOLATE: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.CHOCOLATE)

        @JvmField
        val STORAGE_BLOCKS_MEAT: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.MEAT)

        @JvmField
        val STORAGE_BLOCKS_COOKED_MEAT: TagKey<Item> = commonTag(STORAGE_BLOCKS, RagiumConstantValues.COOKED_MEAT)

        // Tools
        @JvmField
        val TOOLS_FORGE_HAMMER: TagKey<Item> = commonTag("tools", "forge_hammer")

        // Mekanism Integration
        @JvmField
        val ENRICHED_RAGINITE: TagKey<Item> = commonTag(RagiumConstantValues.ENRICHED, RagiumConstantValues.RAGINITE)

        @JvmField
        val ENRICHED_AZURE: TagKey<Item> = commonTag(RagiumConstantValues.ENRICHED, "azure")

        @JvmStatic
        private fun commonTag(path: String): TagKey<Item> = itemTagKey(commonId(path))

        @JvmStatic
        private fun commonTag(prefix: String, value: String): TagKey<Item> = itemTagKey(commonId(prefix, value))
    }
}
