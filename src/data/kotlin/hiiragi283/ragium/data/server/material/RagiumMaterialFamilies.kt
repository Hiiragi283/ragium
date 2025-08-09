package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.HTMaterialFamily
import hiiragi283.ragium.api.util.HTMaterialType
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object RagiumMaterialFamilies {
    // Gems
    @JvmField
    val RAGI_CRYSTAL: HTMaterialFamily = gem(HTMaterialType.RAGI_CRYSTAL)

    @JvmField
    val AZURE: HTMaterialFamily = gem(HTMaterialType.AZURE)

    @JvmField
    val CRIMSON_CRYSTAL: HTMaterialFamily = gem(HTMaterialType.CRIMSON_CRYSTAL)

    @JvmField
    val WARPED_CRYSTAL: HTMaterialFamily = gem(HTMaterialType.WARPED_CRYSTAL)

    @JvmField
    val ELDRITCH_PEARL: HTMaterialFamily = gem(HTMaterialType.ELDRITCH_PEARL)

    // Ingots
    @JvmField
    val RAGI_ALLOY: HTMaterialFamily = ingotAlloy(HTMaterialType.RAGI_ALLOY)

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTMaterialFamily = ingotAlloy(HTMaterialType.ADVANCED_RAGI_ALLOY)

    @JvmField
    val AZURE_STEEL: HTMaterialFamily = ingotAlloy(HTMaterialType.AZURE_STEEL)

    @JvmField
    val DEEP_STEEL: HTMaterialFamily = ingotAlloy(HTMaterialType.DEEP_STEEL)

    // Others
    @JvmField
    val CHOCOLATE: HTMaterialFamily = ingotAlloy(HTMaterialType.CHOCOLATE)

    @JvmField
    val MEAT: HTMaterialFamily = ingotAlloy(HTMaterialType.MEAT)

    @JvmField
    val COOKED_MEAT: HTMaterialFamily = ingotAlloy(HTMaterialType.COOKED_MEAT)

    @JvmStatic
    private fun gem(type: HTMaterialType): HTMaterialFamily = HTMaterialFamily.Builder
        .gem(HTMaterialType.getFromVariant<RagiumItems.Gems>(type))
        .setDefaultedEntry(
            HTMaterialFamily.Variant.DUSTS,
            HTMaterialType.getFromVariant<RagiumItems.Dusts>(type),
        ).setDefaultedEntry(
            HTMaterialFamily.Variant.STORAGE_BLOCKS,
            HTMaterialType.getFromVariant<RagiumBlocks.StorageBlocks>(type),
        ).build(type.serializedName)

    @JvmStatic
    private fun ingot(type: HTMaterialType): HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(HTMaterialType.getFromVariant<RagiumItems.Ingots>(type))
        .setDefaultedEntry(
            HTMaterialFamily.Variant.DUSTS,
            HTMaterialType.getFromVariant<RagiumItems.Dusts>(type),
        ).setDefaultedEntry(
            HTMaterialFamily.Variant.NUGGETS,
            HTMaterialType.getFromVariant<RagiumItems.Nuggets>(type),
        ).setDefaultedEntry(
            HTMaterialFamily.Variant.STORAGE_BLOCKS,
            HTMaterialType.getFromVariant<RagiumBlocks.StorageBlocks>(type),
        ).build(type.serializedName)

    @JvmStatic
    private fun ingotAlloy(type: HTMaterialType): HTMaterialFamily = HTMaterialFamily.Builder
        .ingotAlloy(HTMaterialType.getFromVariant<RagiumItems.Ingots>(type))
        .setDefaultedEntry(
            HTMaterialFamily.Variant.DUSTS,
            HTMaterialType.getFromVariant<RagiumItems.Dusts>(type),
        ).setDefaultedEntry(
            HTMaterialFamily.Variant.NUGGETS,
            HTMaterialType.getFromVariant<RagiumItems.Nuggets>(type),
        ).setDefaultedEntry(
            HTMaterialFamily.Variant.STORAGE_BLOCKS,
            HTMaterialType.getFromVariant<RagiumBlocks.StorageBlocks>(type),
        ).build(type.serializedName)
}
