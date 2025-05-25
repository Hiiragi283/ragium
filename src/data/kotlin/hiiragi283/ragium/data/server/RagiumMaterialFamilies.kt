package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumMaterialFamilies {
    //    Vanilla    //

    @JvmField
    val COPPER: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Tags.Items.INGOTS_COPPER, Items.COPPER_INGOT)
        .setEntry(HTMaterialFamily.Variant.ORES, Tags.Items.ORES_COPPER, Items.COPPER_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Tags.Items.RAW_MATERIALS_COPPER, Items.RAW_COPPER)
        .setEntry(HTMaterialFamily.Variant.RAW_STORAGE, Tags.Items.STORAGE_BLOCKS_RAW_COPPER, Items.RAW_COPPER_BLOCK)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCK, Tags.Items.STORAGE_BLOCKS_COPPER, Items.COPPER_BLOCK)
        .setVanilla()
        .build()

    @JvmField
    val IRON: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Tags.Items.INGOTS_IRON, Items.IRON_INGOT)
        .setEntry(HTMaterialFamily.Variant.NUGGETS, Tags.Items.NUGGETS_IRON, Items.IRON_NUGGET)
        .setEntry(HTMaterialFamily.Variant.ORES, Tags.Items.ORES_IRON, Items.IRON_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Tags.Items.RAW_MATERIALS_IRON, Items.RAW_IRON)
        .setEntry(HTMaterialFamily.Variant.RAW_STORAGE, Tags.Items.STORAGE_BLOCKS_RAW_IRON, Items.RAW_IRON_BLOCK)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCK, Tags.Items.STORAGE_BLOCKS_IRON, Items.IRON_BLOCK)
        .setVanilla()
        .build()

    @JvmField
    val GOLD: HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(Tags.Items.INGOTS_GOLD, Items.GOLD_INGOT)
        .setEntry(HTMaterialFamily.Variant.NUGGETS, Tags.Items.NUGGETS_GOLD, Items.GOLD_NUGGET)
        .setEntry(HTMaterialFamily.Variant.ORES, Tags.Items.ORES_GOLD, Items.GOLD_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Tags.Items.RAW_MATERIALS_GOLD, Items.RAW_GOLD)
        .setEntry(HTMaterialFamily.Variant.RAW_STORAGE, Tags.Items.STORAGE_BLOCKS_RAW_GOLD, Items.RAW_GOLD_BLOCK)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCK, Tags.Items.STORAGE_BLOCKS_GOLD, Items.GOLD_BLOCK)
        .setVanilla()
        .build()

    @JvmField
    val COAL: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(ItemTags.COALS, Items.COAL)
        .setEntry(HTMaterialFamily.Variant.ORES, Tags.Items.ORES_COAL, Items.COAL_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, ItemTags.COALS, Items.COAL)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCK, Tags.Items.STORAGE_BLOCKS_COAL, Items.COAL_BLOCK)
        .setVanilla()
        .build()

    @JvmField
    val LAPIS: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Tags.Items.GEMS_LAPIS, Items.LAPIS_LAZULI)
        .setEntry(HTMaterialFamily.Variant.ORES, Tags.Items.ORES_LAPIS, Items.LAPIS_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Tags.Items.GEMS_LAPIS, Items.LAPIS_LAZULI)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCK, Tags.Items.STORAGE_BLOCKS_LAPIS, Items.LAPIS_BLOCK)
        .setVanilla()
        .build()

    @JvmField
    val QUARTZ: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Tags.Items.GEMS_QUARTZ, Items.QUARTZ)
        .setEntry(HTMaterialFamily.Variant.ORES, Tags.Items.ORES_QUARTZ, Items.NETHER_QUARTZ_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Tags.Items.GEMS_QUARTZ, Items.QUARTZ)
        .setVanilla()
        .build()

    @JvmField
    val DIAMOND: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Tags.Items.GEMS_DIAMOND, Items.DIAMOND)
        .setEntry(HTMaterialFamily.Variant.ORES, Tags.Items.ORES_DIAMOND, Items.DIAMOND_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Tags.Items.GEMS_DIAMOND, Items.DIAMOND)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCK, Tags.Items.STORAGE_BLOCKS_DIAMOND, Items.DIAMOND_BLOCK)
        .setVanilla()
        .build()

    @JvmField
    val EMERALD: HTMaterialFamily = HTMaterialFamily.Builder
        .gem(Tags.Items.GEMS_EMERALD, Items.EMERALD)
        .setEntry(HTMaterialFamily.Variant.ORES, Tags.Items.ORES_EMERALD, Items.EMERALD_ORE)
        .setEntry(HTMaterialFamily.Variant.RAW_MATERIALS, Tags.Items.GEMS_EMERALD, Items.EMERALD)
        .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCK, Tags.Items.STORAGE_BLOCKS_EMERALD, Items.EMERALD_BLOCK)
        .setVanilla()
        .build()

    //    Ragium    //

    // Gems
    @JvmField
    val RAGI_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItemTags.GEMS_RAGI_CRYSTAL, RagiumItems.RAGI_CRYSTAL)
            .setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_RAGI_CRYSTAL,
                RagiumBlocks.RAGI_CRYSTAL_BLOCK,
            ).build()

    @JvmField
    val CRIMSON_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItemTags.GEMS_CRIMSON_CRYSTAL, RagiumItems.CRIMSON_CRYSTAL)
            .setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_CRIMSON_CRYSTAL,
                RagiumBlocks.CRIMSON_CRYSTAL_BLOCK,
            ).build()

    @JvmField
    val WARPED_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItemTags.GEMS_WARPED_CRYSTAL, RagiumItems.WARPED_CRYSTAL)
            .setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_WARPED_CRYSTAL,
                RagiumBlocks.WARPED_CRYSTAL_BLOCK,
            ).build()

    @JvmField
    val ELDRITCH_PEARL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItemTags.GEMS_ELDRITCH_PEARL, RagiumItems.ELDRITCH_PEARL)
            .setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_ELDRITCH_PEARL,
                RagiumBlocks.ELDRITCH_PEARL_BLOCK,
            ).build()

    // Ingots
    @JvmField
    val RAGI_ALLOY: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItemTags.INGOTS_RAGI_ALLOY, RagiumItems.RAGI_ALLOY_INGOT)
            .setEntry(
                HTMaterialFamily.Variant.NUGGETS,
                RagiumItemTags.NUGGETS_RAGI_ALLOY,
                RagiumItems.RAGI_ALLOY_NUGGET,
            ).setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_RAGI_ALLOY,
                RagiumBlocks.RAGI_ALLOY_BLOCK,
            ).build()

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItemTags.INGOTS_ADVANCED_RAGI_ALLOY, RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            .setEntry(
                HTMaterialFamily.Variant.NUGGETS,
                RagiumItemTags.NUGGETS_ADVANCED_RAGI_ALLOY,
                RagiumItems.ADVANCED_RAGI_ALLOY_NUGGET,
            ).setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_ADVANCED_RAGI_ALLOY,
                RagiumBlocks.ADVANCED_RAGI_ALLOY_BLOCK,
            ).build()

    @JvmField
    val AZURE_STEEL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItemTags.INGOTS_AZURE_STEEL, RagiumItems.AZURE_STEEL_INGOT)
            .setEntry(
                HTMaterialFamily.Variant.NUGGETS,
                RagiumItemTags.NUGGETS_AZURE_STEEL,
                RagiumItems.AZURE_STEEL_NUGGET,
            ).setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_AZURE_STEEL,
                RagiumBlocks.AZURE_STEEL_BLOCK,
            ).build()

    @JvmField
    val DEEP_STEEL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItemTags.INGOTS_DEEP_STEEL, RagiumItems.DEEP_STEEL_INGOT)
            .setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_DEEP_STEEL,
                RagiumBlocks.DEEP_STEEL_BLOCK,
            ).build()

    // Others
    @JvmField
    val CHOCOLATE: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItemTags.INGOTS_CHOCOLATE, RagiumItems.CHOCOLATE_INGOT)
            .setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_CHOCOLATE,
                RagiumBlocks.CHOCOLATE_BLOCK,
            ).build()

    @JvmField
    val MEAT: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItemTags.INGOTS_MEAT, RagiumItems.MEAT_INGOT)
            .setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_MEAT,
                RagiumBlocks.MEAT_BLOCK,
            ).build()

    @JvmField
    val COOKED_MEAT: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItemTags.INGOTS_COOKED_MEAT, RagiumItems.COOKED_MEAT_INGOT)
            .setEntry(
                HTMaterialFamily.Variant.STORAGE_BLOCK,
                RagiumItemTags.STORAGE_BLOCKS_COOKED_MEAT,
                RagiumBlocks.COOKED_MEAT_BLOCK,
            ).build()
}
