package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object RagiumMaterialFamilies {
    // Gems
    @JvmField
    val RAGI_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.RAGI_CRYSTAL)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.RAGI_CRYSTAL_BLOCK)
            .build(RagiumConstantValues.RAGI_CRYSTAL)

    @JvmField
    val CRIMSON_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.CRIMSON_CRYSTAL)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.CRIMSON_CRYSTAL_BLOCK)
            .build(RagiumConstantValues.CRIMSON_CRYSTAL)

    @JvmField
    val WARPED_CRYSTAL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.WARPED_CRYSTAL)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.WARPED_CRYSTAL_BLOCK)
            .build(RagiumConstantValues.WARPED_CRYSTAL)

    @JvmField
    val ELDRITCH_PEARL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .gem(RagiumItems.ELDRITCH_PEARL)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.ELDRITCH_PEARL_BLOCK)
            .build(RagiumConstantValues.ELDRITCH_PEARL)

    // Ingots
    @JvmField
    val RAGI_ALLOY: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItems.RAGI_ALLOY_INGOT)
            .setEntry(HTMaterialFamily.Variant.NUGGETS, RagiumItems.RAGI_ALLOY_NUGGET)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.RAGI_ALLOY_BLOCK)
            .build(RagiumConstantValues.RAGI_ALLOY)

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT)
            .setEntry(HTMaterialFamily.Variant.NUGGETS, RagiumItems.ADVANCED_RAGI_ALLOY_NUGGET)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.ADVANCED_RAGI_ALLOY_BLOCK)
            .build(RagiumConstantValues.ADVANCED_RAGI_ALLOY)

    @JvmField
    val AZURE_STEEL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItems.AZURE_STEEL_INGOT)
            .setEntry(HTMaterialFamily.Variant.NUGGETS, RagiumItems.AZURE_STEEL_NUGGET)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.AZURE_STEEL_BLOCK)
            .build(RagiumConstantValues.AZURE_STEEL)

    @JvmField
    val DEEP_STEEL: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItems.DEEP_STEEL_INGOT)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.DEEP_STEEL_BLOCK)
            .build(RagiumConstantValues.DEEP_STEEL)

    // Others
    @JvmField
    val CHOCOLATE: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItems.CHOCOLATE_INGOT)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.CHOCOLATE_BLOCK)
            .build(RagiumConstantValues.CHOCOLATE)

    @JvmField
    val MEAT: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItems.MEAT_INGOT)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.MEAT_BLOCK)
            .build(RagiumConstantValues.MEAT)

    @JvmField
    val COOKED_MEAT: HTMaterialFamily =
        HTMaterialFamily.Builder
            .ingot(RagiumItems.COOKED_MEAT_INGOT)
            .setEntry(HTMaterialFamily.Variant.STORAGE_BLOCKS, RagiumBlocks.COOKED_MEAT_BLOCK)
            .build(RagiumConstantValues.COOKED_MEAT)
}
