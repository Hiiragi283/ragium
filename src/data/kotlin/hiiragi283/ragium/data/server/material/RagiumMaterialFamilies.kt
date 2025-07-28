package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object RagiumMaterialFamilies {
    // Gems
    @JvmField
    val RAGI_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.RAGI_CRYSTAL)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.RAGI_CRYSTAL)
            .build(RagiumConst.RAGI_CRYSTAL)

    @JvmField
    val AZURE: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.AZURE_SHARD)
            .build("azure")

    @JvmField
    val CRIMSON_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.CRIMSON_CRYSTAL)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.CRIMSON_CRYSTAL)
            .build(RagiumConst.CRIMSON_CRYSTAL)

    @JvmField
    val WARPED_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.WARPED_CRYSTAL)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.WARPED_CRYSTAL)
            .build(RagiumConst.WARPED_CRYSTAL)

    @JvmField
    val ELDRITCH_PEARL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.ELDRITCH_PEARL)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.ELDRITCH_PEARL)
            .build(RagiumConst.ELDRITCH_PEARL)

    // Ingots
    @JvmField
    val RAGI_ALLOY: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingotAlloy(RagiumItems.RAGI_ALLOY_INGOT)
            .setDefaultedEntry(HTMaterialFamily.Variant.NUGGETS, RagiumItems.RAGI_ALLOY_NUGGET)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.RAGI_ALLOY)
            .build(RagiumConst.RAGI_ALLOY)

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingotAlloy(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            .setDefaultedEntry(HTMaterialFamily.Variant.NUGGETS, RagiumItems.ADVANCED_RAGI_ALLOY_NUGGET)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.ADVANCED_RAGI_ALLOY)
            .build(RagiumConst.ADVANCED_RAGI_ALLOY)

    @JvmField
    val AZURE_STEEL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingotAlloy(RagiumItems.AZURE_STEEL_INGOT)
            .setDefaultedEntry(HTMaterialFamily.Variant.NUGGETS, RagiumItems.AZURE_STEEL_NUGGET)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.AZURE_STEEL)
            .build(RagiumConst.AZURE_STEEL)

    @JvmField
    val DEEP_STEEL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingotAlloy(RagiumItems.DEEP_STEEL_INGOT)
            .setDefaultedEntry(HTMaterialFamily.Variant.NUGGETS, RagiumItems.DEEP_STEEL_NUGGET)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.DEEP_STEEL)
            .build(RagiumConst.DEEP_STEEL)

    // Others
    @JvmField
    val CHOCOLATE: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingotAlloy(RagiumItems.CHOCOLATE_INGOT)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.CHOCOLATE)
            .build(RagiumConst.CHOCOLATE)

    @JvmField
    val MEAT: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingotAlloy(RagiumItems.MEAT_INGOT)
            .setDefaultedEntry(HTMaterialFamily.Variant.DUSTS, RagiumItems.MINCED_MEAT)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.MEAT)
            .build(RagiumConst.MEAT)

    @JvmField
    val COOKED_MEAT: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingotAlloy(RagiumItems.COOKED_MEAT_INGOT)
            .setDefaultedEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.StorageBlocks.COOKED_MEAT)
            .build(RagiumConst.COOKED_MEAT)
}
