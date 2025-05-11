package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * Ragiumが使用するアイテムの[TagKey]の一覧
 */
object RagiumItemTags {
    //    Dust    //

    @JvmField
    val DUSTS_ASH: TagKey<Item> = commonTag("dusts/ash")

    @JvmField
    val DUSTS_OBSIDIAN: TagKey<Item> = commonTag("dusts/obsidian")

    @JvmField
    val DUSTS_RAGINITE: TagKey<Item> = commonTag("dusts/raginite")

    @JvmField
    val DUSTS_SALTPETER: TagKey<Item> = commonTag("dusts/saltpeter")

    @JvmField
    val DUSTS_SULFUR: TagKey<Item> = commonTag("dusts/sulfur")

    @JvmField
    val DUSTS_WOOD: TagKey<Item> = commonTag("dusts/wood")

    //    Gems    //

    @JvmField
    val GEMS_COAL: TagKey<Item> = commonTag("gems/coal")

    @JvmField
    val GEMS_RAGI_CRYSTAL: TagKey<Item> = commonTag("gems/ragi_crystal")

    @JvmField
    val GEMS_CRIMSON_CRYSTAL: TagKey<Item> = commonTag("gems/crimson_crystal")

    @JvmField
    val GEMS_WARPED_CRYSTAL: TagKey<Item> = commonTag("gems/warped_crystal")

    //    Ingots    //

    @JvmField
    val INGOTS_RAGI_ALLOY: TagKey<Item> = commonTag("ingots/ragi_alloy")

    @JvmField
    val INGOTS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag("ingots/advanced_ragi_alloy")

    @JvmField
    val INGOTS_AZURE_STEEL: TagKey<Item> = commonTag("ingots/azure_steel")

    @JvmField
    val INGOTS_DEEP_STEEL: TagKey<Item> = commonTag("ingots/deep_steel")

    @JvmField
    val INGOTS_CHEESE: TagKey<Item> = commonTag("ingots/cheese")

    @JvmField
    val INGOTS_CHOCOLATE: TagKey<Item> = commonTag("ingots/chocolate")

    //    Ores    //

    @JvmField
    val ORES_RAGINITE: TagKey<Item> = commonTag("ores/raginite")

    //    Storage Blocks    //

    @JvmField
    val STORAGE_BLOCKS_RAGI_CRYSTAL: TagKey<Item> = commonTag("storage_blocks/ragi_crystal")

    @JvmField
    val STORAGE_BLOCKS_CRIMSON_CRYSTAL: TagKey<Item> = commonTag("storage_blocks/crimson_crystal")

    @JvmField
    val STORAGE_BLOCKS_WARPED_CRYSTAL: TagKey<Item> = commonTag("storage_blocks/warped_crystal")

    @JvmField
    val STORAGE_BLOCKS_RAGI_ALLOY: TagKey<Item> = commonTag("storage_blocks/ragi_alloy")

    @JvmField
    val STORAGE_BLOCKS_ADVANCED_RAGI_ALLOY: TagKey<Item> = commonTag("storage_blocks/advanced_ragi_alloy")

    @JvmField
    val STORAGE_BLOCKS_AZURE_STEEL: TagKey<Item> = commonTag("storage_blocks/azure_steel")

    @JvmField
    val STORAGE_BLOCKS_DEEP_STEEL: TagKey<Item> = commonTag("storage_blocks/deep_steel")

    @JvmField
    val STORAGE_BLOCKS_CHEESE: TagKey<Item> = commonTag("storage_blocks/cheese")

    @JvmField
    val STORAGE_BLOCKS_CHOCOLATE: TagKey<Item> = commonTag("storage_blocks/chocolate")

    //    Mekanism Integration    //

    @JvmField
    val ENRICHED_RAGINITE: TagKey<Item> = commonTag("enriched/raginite")

    @JvmField
    val ENRICHED_AZURE: TagKey<Item> = commonTag("enriched/azure")

    //    Common    //

    @JvmStatic
    private fun commonTag(path: String): TagKey<Item> = itemTagKey(commonId(path))

    @JvmField
    val COAL_COKE: TagKey<Item> = commonTag("coal_coke")

    @JvmField
    val PAPER: TagKey<Item> = commonTag("paper")

    @JvmField
    val PLASTICS: TagKey<Item> = commonTag("plastics")

    @JvmField
    val SILICON: TagKey<Item> = commonTag("silicon")

    @JvmField
    val SLAG: TagKey<Item> = commonTag("slag")

    @JvmField
    val TOOLS_KNIFE: TagKey<Item> = commonTag("tools/knife")

    @JvmField
    val TOOLS_FORGE_HAMMER: TagKey<Item> = commonTag("tools/forge_hammer")

    // Foods
    @JvmField
    val CROPS_WARPED_WART: TagKey<Item> = commonTag("crops/warped_wart")

    @JvmField
    val FLOURS: TagKey<Item> = commonTag("flours")

    @JvmField
    val FOODS_CHEESE: TagKey<Item> = commonTag("foods/cheese")

    @JvmField
    val FOODS_CHOCOLATE: TagKey<Item> = commonTag("foods/chocolate")

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

    // Circuits
    @JvmField
    val CIRCUITS: TagKey<Item> = itemTagKey(commonId("circuits"))

    @JvmField
    val CIRCUITS_BASIC: TagKey<Item> = itemTagKey(commonId("circuits/basic"))

    @JvmField
    val CIRCUITS_ADVANCED: TagKey<Item> = itemTagKey(commonId("circuits/advanced"))

    @JvmField
    val CIRCUITS_ELITE: TagKey<Item> = itemTagKey(commonId("circuits/elite"))

    // Glasses
    @JvmField
    val GLASS_BLOCKS_OBSIDIAN: TagKey<Item> = commonTag("glass_blocks/obsidian")

    @JvmField
    val GLASS_BLOCKS_QUARTZ: TagKey<Item> = commonTag("glass_blocks/quartz")

    @JvmField
    val GLASS_BLOCKS_SOUL: TagKey<Item> = commonTag("glass_blocks/soul")

    // Stones
    @JvmField
    val OBSIDIANS_MYSTERIOUS: TagKey<Item> = commonTag("obsidians/mysterious")

    //    Ragium    //

    @JvmField
    val CAPACITY_ENCHANTABLE: TagKey<Item> = itemTagKey(RagiumAPI.id("enchantable/capacity"))

    @JvmField
    val DYNAMITES: TagKey<Item> = itemTagKey(RagiumAPI.id("dynamites"))

    @JvmField
    val LED_BLOCKS: TagKey<Item> = itemTagKey(RagiumAPI.id("led_blocks"))

    // Molds
    @JvmField
    val MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds"))

    @JvmField
    val MOLDS_BALL: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/ball"))

    @JvmField
    val MOLDS_BLANK: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/blank"))

    @JvmField
    val MOLDS_BLOCK: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/block"))

    @JvmField
    val MOLDS_GEAR: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/gear"))

    @JvmField
    val MOLDS_INGOT: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/ingot"))

    @JvmField
    val MOLDS_PLATE: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/plate"))

    @JvmField
    val MOLDS_ROD: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/rod"))

    @JvmField
    val MOLDS_WIRE: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/wire"))

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
