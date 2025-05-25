package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * Ragiumが使用するアイテムの[TagKey]の一覧
 */
object RagiumItemTags {
    //    Circuits    //

    @JvmField
    val CIRCUITS: TagKey<Item> = commonTag("circuits")

    @JvmField
    val CIRCUITS_BASIC: TagKey<Item> = commonTag("circuits/basic")

    @JvmField
    val CIRCUITS_ADVANCED: TagKey<Item> = commonTag("circuits/advanced")

    @JvmField
    val CIRCUITS_ELITE: TagKey<Item> = commonTag("circuits/elite")

    //    Dusts    //

    @JvmField
    val DUSTS_ASH: TagKey<Item> = commonTag(RagiumConstantValues.DUSTS, "ash")

    @JvmField
    val DUSTS_OBSIDIAN: TagKey<Item> = commonTag(RagiumConstantValues.DUSTS, RagiumConstantValues.OBSIDIAN)

    @JvmField
    val DUSTS_RAGINITE: TagKey<Item> = commonTag(RagiumConstantValues.DUSTS, RagiumConstantValues.RAGINITE)

    @JvmField
    val DUSTS_SALTPETER: TagKey<Item> = commonTag("${RagiumConstantValues.DUSTS}/saltpeter")

    @JvmField
    val DUSTS_SULFUR: TagKey<Item> = commonTag("${RagiumConstantValues.DUSTS}/sulfur")

    @JvmField
    val DUSTS_WOOD: TagKey<Item> = commonTag("${RagiumConstantValues.DUSTS}/wood")

    //    Gems    //

    @JvmField
    val GEMS_RAGI_CRYSTAL: TagKey<Item> = commonTag(RagiumConstantValues.GEMS, RagiumConstantValues.RAGI_CRYSTAL)

    @JvmField
    val GEMS_CRIMSON_CRYSTAL: TagKey<Item> = commonTag(RagiumConstantValues.GEMS, RagiumConstantValues.CRIMSON_CRYSTAL)

    @JvmField
    val GEMS_WARPED_CRYSTAL: TagKey<Item> = commonTag(RagiumConstantValues.GEMS, RagiumConstantValues.WARPED_CRYSTAL)

    @JvmField
    val GEMS_ELDRITCH_PEARL: TagKey<Item> = commonTag(RagiumConstantValues.GEMS, RagiumConstantValues.ELDRITCH_PEARL)

    //    Ingots    //

    @JvmField
    val INGOTS_RAGI_ALLOY: TagKey<Item> = commonTag(RagiumConstantValues.INGOTS, RagiumConstantValues.RAGI_ALLOY)

    @JvmField
    val INGOTS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag(RagiumConstantValues.INGOTS, RagiumConstantValues.ADVANCED_RAGI_ALLOY)

    @JvmField
    val INGOTS_AZURE_STEEL: TagKey<Item> = commonTag(RagiumConstantValues.INGOTS, RagiumConstantValues.AZURE_STEEL)

    @JvmField
    val INGOTS_DEEP_STEEL: TagKey<Item> = commonTag(RagiumConstantValues.INGOTS, RagiumConstantValues.DEEP_STEEL)

    @JvmField
    val INGOTS_CHOCOLATE: TagKey<Item> = commonTag(RagiumConstantValues.INGOTS, RagiumConstantValues.CHOCOLATE)

    @JvmField
    val INGOTS_MEAT: TagKey<Item> = commonTag(RagiumConstantValues.INGOTS, RagiumConstantValues.MEAT)

    @JvmField
    val INGOTS_COOKED_MEAT: TagKey<Item> = commonTag(RagiumConstantValues.INGOTS, RagiumConstantValues.COOKED_MEAT)

    @JvmField
    val BEACON_PAYMENTS: List<TagKey<Item>> = listOf(
        // gems
        GEMS_RAGI_CRYSTAL,
        GEMS_CRIMSON_CRYSTAL,
        GEMS_WARPED_CRYSTAL,
        GEMS_ELDRITCH_PEARL,
        // ingots
        INGOTS_RAGI_ALLOY,
        INGOTS_ADVANCED_RAGI_ALLOY,
        INGOTS_AZURE_STEEL,
        INGOTS_DEEP_STEEL,
        INGOTS_CHOCOLATE,
        INGOTS_MEAT,
        INGOTS_COOKED_MEAT,
    )

    //    Molds    //

    // val MOLDS: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS)

    // val MOLDS_BALL: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS, "ball")

    // val MOLDS_BLANK: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS, "blank")

    // val MOLDS_BLOCK: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS, "block")

    // val MOLDS_GEAR: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS, "gear")

    // val MOLDS_INGOT: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS, "ingot")

    // val MOLDS_PLATE: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS, "plate")

    // val MOLDS_ROD: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS, "rod")

    // val MOLDS_WIRE: TagKey<Item> = commonTag(RagiumConstantValues.MOLDS, "wire")

    //    Nuggets    //

    @JvmField
    val NUGGETS_RAGI_ALLOY: TagKey<Item> = commonTag(RagiumConstantValues.NUGGETS, RagiumConstantValues.RAGI_ALLOY)

    @JvmField
    val NUGGETS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag(RagiumConstantValues.NUGGETS, RagiumConstantValues.ADVANCED_RAGI_ALLOY)

    @JvmField
    val NUGGETS_AZURE_STEEL: TagKey<Item> = commonTag(RagiumConstantValues.NUGGETS, RagiumConstantValues.AZURE_STEEL)

    //    Ores    //

    @JvmField
    val ORES_RAGINITE: TagKey<Item> = commonTag(RagiumConstantValues.ORES, RagiumConstantValues.RAGINITE)

    @JvmField
    val ORES_RAGI_CRYSTAL: TagKey<Item> = commonTag(RagiumConstantValues.ORES, RagiumConstantValues.RAGI_CRYSTAL)

    //    Storage Blocks    //

    @JvmField
    val STORAGE_BLOCKS_RAGI_CRYSTAL: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.RAGI_CRYSTAL)

    @JvmField
    val STORAGE_BLOCKS_CRIMSON_CRYSTAL: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.CRIMSON_CRYSTAL)

    @JvmField
    val STORAGE_BLOCKS_WARPED_CRYSTAL: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.WARPED_CRYSTAL)

    @JvmField
    val STORAGE_BLOCKS_ELDRITCH_PEARL: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.ELDRITCH_PEARL)

    @JvmField
    val STORAGE_BLOCKS_RAGI_ALLOY: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.RAGI_ALLOY)

    @JvmField
    val STORAGE_BLOCKS_ADVANCED_RAGI_ALLOY: TagKey<Item> =
        commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.ADVANCED_RAGI_ALLOY)

    @JvmField
    val STORAGE_BLOCKS_AZURE_STEEL: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.AZURE_STEEL)

    @JvmField
    val STORAGE_BLOCKS_DEEP_STEEL: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.DEEP_STEEL)

    @JvmField
    val STORAGE_BLOCKS_CHOCOLATE: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.CHOCOLATE)

    @JvmField
    val STORAGE_BLOCKS_MEAT: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.MEAT)

    @JvmField
    val STORAGE_BLOCKS_COOKED_MEAT: TagKey<Item> = commonTag(RagiumConstantValues.STORAGE_BLOCKS, RagiumConstantValues.COOKED_MEAT)

    //    Mekanism Integration    //

    @JvmField
    val ENRICHED_RAGINITE: TagKey<Item> = commonTag(RagiumConstantValues.ENRICHED, RagiumConstantValues.RAGINITE)

    @JvmField
    val ENRICHED_AZURE: TagKey<Item> = commonTag(RagiumConstantValues.ENRICHED, "azure")

    @JvmStatic
    private fun commonTag(path: String): TagKey<Item> = itemTagKey(commonId(path))

    @JvmStatic
    private fun commonTag(prefix: String, value: String): TagKey<Item> = itemTagKey(commonId(prefix, value))

    //    Common    //

    @JvmField
    val PAPER: TagKey<Item> = commonTag("paper")

    @JvmField
    val PLASTICS: TagKey<Item> = commonTag("plastics")

    @JvmField
    val SILICON: TagKey<Item> = commonTag("silicon")

    @JvmField
    val TOOLS_FORGE_HAMMER: TagKey<Item> = commonTag("tools/forge_hammer")

    // Foods
    @JvmField
    val CROPS_WARPED_WART: TagKey<Item> = commonTag("crops/warped_wart")

    @JvmField
    val FLOURS: TagKey<Item> = commonTag("flours")

    @JvmField
    val FOODS_CHOCOLATE: TagKey<Item> = commonTag("foods", RagiumConstantValues.CHOCOLATE)

    // Cherry
    @JvmField
    val FOODS_CHERRY: TagKey<Item> = commonTag("foods/cherry")

    @JvmField
    val FOODS_RAGI_CHERRY: TagKey<Item> = commonTag("foods/ragi_cherry")

    // Jam
    @JvmField
    val FOODS_JAMS: TagKey<Item> = commonTag("foods/jams")

    @JvmField
    val JAMS_RAGI_CHERRY: TagKey<Item> = commonTag("jams/ragi_cherry")

    // Glasses
    @JvmField
    val GLASS_BLOCKS_OBSIDIAN: TagKey<Item> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.OBSIDIAN)

    @JvmField
    val GLASS_BLOCKS_QUARTZ: TagKey<Item> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.QUARTZ)

    @JvmField
    val GLASS_BLOCKS_SOUL: TagKey<Item> = commonTag(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.SOUL)

    // Stones
    @JvmField
    val OBSIDIANS_MYSTERIOUS: TagKey<Item> = commonTag("obsidians/mysterious")

    //    Ragium    //

    @JvmField
    val DYNAMITES: TagKey<Item> = itemTagKey(RagiumAPI.id("dynamites"))

    @JvmField
    val ELDRITCH_PEARL_BINDER: TagKey<Item> = itemTagKey(RagiumAPI.id("eldritch_pearl_binder"))

    @JvmField
    val LED_BLOCKS: TagKey<Item> = itemTagKey(RagiumAPI.id("led_blocks"))

    // Soils
    @JvmField
    val DIRT_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/dirt"))

    @JvmField
    val MUSHROOM_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/mushroom"))

    @JvmField
    val NETHER_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/nether"))

    @JvmField
    val END_SOILS: TagKey<Item> = itemTagKey(RagiumAPI.id("soils/end"))
}
