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
        val GLASS_BLOCKS_OBSIDIAN: TagKey<Block> = create(RagiumConst.GLASS_BLOCKS, RagiumConst.OBSIDIAN)

        @JvmField
        val GLASS_BLOCKS_QUARTZ: TagKey<Block> = create(RagiumConst.GLASS_BLOCKS, RagiumConst.QUARTZ)

        @JvmField
        val GLASS_BLOCKS_SOUL: TagKey<Block> = create(RagiumConst.GLASS_BLOCKS, RagiumConst.SOUL)

        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Block> = create("obsidians", "mysterious")

        @JvmField
        val ORES_RAGINITE: TagKey<Block> = create(RagiumConst.ORES, RagiumConst.RAGINITE)

        @JvmField
        val ORES_RAGI_CRYSTAL: TagKey<Block> = create(RagiumConst.ORES, RagiumConst.RAGI_CRYSTAL)

        @JvmField
        val ORES_CRIMSON_CRYSTAL: TagKey<Block> = create(RagiumConst.ORES, RagiumConst.CRIMSON_CRYSTAL)

        @JvmField
        val ORES_WARPED_CRYSTAL: TagKey<Block> = create(RagiumConst.ORES, RagiumConst.WARPED_CRYSTAL)

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
        val CREOSOTE: TagKey<Fluid> = create("creosote")

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

        @JvmField
        val PAPER: TagKey<Item> = create("paper")

        @JvmField
        val SILICON: TagKey<Item> = create("silicon")

        // Circuits
        @JvmField
        val CIRCUITS: TagKey<Item> = create(RagiumConst.CIRCUITS)

        @JvmField
        val CIRCUITS_BASIC: TagKey<Item> = create(RagiumConst.CIRCUITS, "basic")

        @JvmField
        val CIRCUITS_ADVANCED: TagKey<Item> = create(RagiumConst.CIRCUITS, "advanced")

        @JvmField
        val CIRCUITS_ELITE: TagKey<Item> = create(RagiumConst.CIRCUITS, "elite")

        @JvmField
        val CIRCUITS_ULTIMATE: TagKey<Item> = create(RagiumConst.CIRCUITS, "ultimate")

        // Crops
        @JvmField
        val CROPS_WARPED_WART: TagKey<Item> = create("crops", "warped_wart")

        // Dusts
        private const val DUSTS: String = RagiumConst.DUSTS

        @JvmField
        val DUSTS_ASH: TagKey<Item> = create(DUSTS, "ash")

        @JvmField
        val DUSTS_OBSIDIAN: TagKey<Item> = create(DUSTS, RagiumConst.OBSIDIAN)

        @JvmField
        val DUSTS_RAGINITE: TagKey<Item> = create(DUSTS, RagiumConst.RAGINITE)

        @JvmField
        val DUSTS_CHARCOAL: TagKey<Item> = create(DUSTS, "charcoal")

        @JvmField
        val DUSTS_CINNABAR: TagKey<Item> = create(DUSTS, "cinnabar")

        @JvmField
        val DUSTS_COAL: TagKey<Item> = create(DUSTS, "coal")

        @JvmField
        val DUSTS_SALTPETER: TagKey<Item> = create(DUSTS, "saltpeter")

        @JvmField
        val DUSTS_SULFUR: TagKey<Item> = create(DUSTS, "sulfur")

        @JvmField
        val DUSTS_MEAT: TagKey<Item> = create(DUSTS, "meat")

        @JvmField
        val DUSTS_WOOD: TagKey<Item> = create(DUSTS, "wood")

        // Foods
        @JvmField
        val FOODS_CHOCOLATE: TagKey<Item> = create("foods", RagiumConst.CHOCOLATE)

        // Foods - Cherry
        @JvmField
        val FOODS_CHERRY: TagKey<Item> = create("foods", "cherry")

        @JvmField
        val FOODS_RAGI_CHERRY: TagKey<Item> = create("foods", "ragi_cherry")

        // Foods - Jam
        @JvmField
        val FOODS_JAMS: TagKey<Item> = create("foods", "jams")

        @JvmField
        val JAMS_RAGI_CHERRY: TagKey<Item> = create("jams", "ragi_cherry")

        // Fuels
        @JvmField
        val FUELS: TagKey<Item> = create(RagiumConst.FUELS)

        @JvmField
        val FUELS_COAL: TagKey<Item> = create(RagiumConst.FUELS, "coal")

        @JvmField
        val FUELS_CHARCOAL: TagKey<Item> = create(RagiumConst.FUELS, "charcoal")

        @JvmField
        val FUELS_COAL_COKE: TagKey<Item> = create(RagiumConst.FUELS, RagiumConst.COAL_COKE)

        // Glasses
        @JvmField
        val GLASS_BLOCKS_OBSIDIAN: TagKey<Item> = create(RagiumConst.GLASS_BLOCKS, RagiumConst.OBSIDIAN)

        @JvmField
        val GLASS_BLOCKS_QUARTZ: TagKey<Item> = create(RagiumConst.GLASS_BLOCKS, RagiumConst.QUARTZ)

        @JvmField
        val GLASS_BLOCKS_SOUL: TagKey<Item> = create(RagiumConst.GLASS_BLOCKS, RagiumConst.SOUL)

        // Gems
        private const val GEMS: String = RagiumConst.GEMS

        @JvmField
        val GEMS_RAGI_CRYSTAL: TagKey<Item> = create(GEMS, RagiumConst.RAGI_CRYSTAL)

        @JvmField
        val GEMS_AZURE: TagKey<Item> = create(GEMS, RagiumConst.AZURE)

        @JvmField
        val GEMS_CRIMSON_CRYSTAL: TagKey<Item> = create(GEMS, RagiumConst.CRIMSON_CRYSTAL)

        @JvmField
        val GEMS_WARPED_CRYSTAL: TagKey<Item> = create(GEMS, RagiumConst.WARPED_CRYSTAL)

        @JvmField
        val GEMS_ELDRITCH_PEARL: TagKey<Item> = create(GEMS, RagiumConst.ELDRITCH_PEARL)

        // Ingots
        private const val INGOTS: String = RagiumConst.INGOTS

        @JvmField
        val INGOTS_RAGI_ALLOY: TagKey<Item> = create(INGOTS, RagiumConst.RAGI_ALLOY)

        @JvmField
        val INGOTS_ADVANCED_RAGI_ALLOY: TagKey<Item> = create(INGOTS, RagiumConst.ADVANCED_RAGI_ALLOY)

        @JvmField
        val INGOTS_AZURE_STEEL: TagKey<Item> = create(INGOTS, RagiumConst.AZURE_STEEL)

        @JvmField
        val INGOTS_DEEP_STEEL: TagKey<Item> = create(INGOTS, RagiumConst.DEEP_STEEL)

        @JvmField
        val INGOTS_CHOCOLATE: TagKey<Item> = create(INGOTS, RagiumConst.CHOCOLATE)

        @JvmField
        val INGOTS_MEAT: TagKey<Item> = create(INGOTS, RagiumConst.MEAT)

        @JvmField
        val INGOTS_COOKED_MEAT: TagKey<Item> = create(INGOTS, RagiumConst.COOKED_MEAT)

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
        val NUGGETS_RAGI_ALLOY: TagKey<Item> = create(NUGGETS, RagiumConst.RAGI_ALLOY)

        @JvmField
        val NUGGETS_ADVANCED_RAGI_ALLOY: TagKey<Item> = create(NUGGETS, RagiumConst.ADVANCED_RAGI_ALLOY)

        @JvmField
        val NUGGETS_AZURE_STEEL: TagKey<Item> = create(NUGGETS, RagiumConst.AZURE_STEEL)

        // Obsidians
        @JvmField
        val OBSIDIANS_MYSTERIOUS: TagKey<Item> = create("obsidians", "mysterious")

        // Ores
        private const val ORES: String = RagiumConst.ORES

        @JvmField
        val ORES_RAGINITE: TagKey<Item> = create(ORES, RagiumConst.RAGINITE)

        @JvmField
        val ORES_RAGI_CRYSTAL: TagKey<Item> = create(ORES, RagiumConst.RAGI_CRYSTAL)

        @JvmField
        val ORES_CRIMSON_CRYSTAL: TagKey<Item> = create(ORES, RagiumConst.CRIMSON_CRYSTAL)

        @JvmField
        val ORES_WARPED_CRYSTAL: TagKey<Item> = create(ORES, RagiumConst.WARPED_CRYSTAL)

        @JvmField
        val ORES_DEEP_SCRAP: TagKey<Item> = create(ORES, "deep_scrap")

        // Plates
        @JvmField
        val PLATES: TagKey<Item> = create(RagiumConst.PLATES)

        @JvmField
        val PLATES_PLASTIC: TagKey<Item> = create(RagiumConst.PLATES, "plastic")

        // Storage Blocks
        private const val STORAGE_BLOCKS: String = RagiumConst.STORAGE_BLOCKS

        @JvmField
        val STORAGE_BLOCKS_RAGI_CRYSTAL: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.RAGI_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_CRIMSON_CRYSTAL: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.CRIMSON_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_WARPED_CRYSTAL: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.WARPED_CRYSTAL)

        @JvmField
        val STORAGE_BLOCKS_ELDRITCH_PEARL: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.ELDRITCH_PEARL)

        @JvmField
        val STORAGE_BLOCKS_RAGI_ALLOY: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.RAGI_ALLOY)

        @JvmField
        val STORAGE_BLOCKS_ADVANCED_RAGI_ALLOY: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.ADVANCED_RAGI_ALLOY)

        @JvmField
        val STORAGE_BLOCKS_AZURE_STEEL: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.AZURE_STEEL)

        @JvmField
        val STORAGE_BLOCKS_DEEP_STEEL: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.DEEP_STEEL)

        @JvmField
        val STORAGE_BLOCKS_CHOCOLATE: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.CHOCOLATE)

        @JvmField
        val STORAGE_BLOCKS_MEAT: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.MEAT)

        @JvmField
        val STORAGE_BLOCKS_COOKED_MEAT: TagKey<Item> = create(STORAGE_BLOCKS, RagiumConst.COOKED_MEAT)

        @JvmStatic
        private fun create(path: String): TagKey<Item> = itemTagKey(commonId(path))

        @JvmStatic
        private fun create(prefix: String, value: String): TagKey<Item> = itemTagKey(commonId("$prefix/$value"))
    }
}
