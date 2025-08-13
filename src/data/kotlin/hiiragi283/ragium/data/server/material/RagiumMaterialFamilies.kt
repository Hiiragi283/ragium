package hiiragi283.ragium.data.server.material

import hiiragi283.ragium.api.util.material.HTMaterialFamily
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

object RagiumMaterialFamilies {
    // Gems
    @JvmField
    val RAGI_CRYSTAL: HTMaterialFamily = gem(RagiumMaterialType.RAGI_CRYSTAL)

    @JvmField
    val AZURE: HTMaterialFamily = gem(RagiumMaterialType.AZURE)

    @JvmField
    val CRIMSON_CRYSTAL: HTMaterialFamily = gem(RagiumMaterialType.CRIMSON_CRYSTAL)

    @JvmField
    val WARPED_CRYSTAL: HTMaterialFamily = gem(RagiumMaterialType.WARPED_CRYSTAL)

    @JvmField
    val ELDRITCH_PEARL: HTMaterialFamily = gem(RagiumMaterialType.ELDRITCH_PEARL)

    // Ingots
    @JvmField
    val RAGI_ALLOY: HTMaterialFamily = ingotAlloy(RagiumMaterialType.RAGI_ALLOY)

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTMaterialFamily = ingotAlloy(RagiumMaterialType.ADVANCED_RAGI_ALLOY)

    @JvmField
    val AZURE_STEEL: HTMaterialFamily = ingotAlloy(RagiumMaterialType.AZURE_STEEL)

    @JvmField
    val DEEP_STEEL: HTMaterialFamily = ingotAlloy(RagiumMaterialType.DEEP_STEEL)

    // Others
    @JvmField
    val CHOCOLATE: HTMaterialFamily = ingotAlloy(RagiumMaterialType.CHOCOLATE)

    @JvmField
    val MEAT: HTMaterialFamily = ingotAlloy(RagiumMaterialType.MEAT)

    @JvmField
    val COOKED_MEAT: HTMaterialFamily = ingotAlloy(RagiumMaterialType.COOKED_MEAT)

    @JvmStatic
    private fun getBlock(type: RagiumMaterialType): (HTMaterialVariant) -> Supplier<out ItemLike>? = { variant ->
        RagiumBlocks.MATERIALS.get(variant, type)
    }

    @JvmStatic
    private fun getItem(type: RagiumMaterialType): (HTMaterialVariant) -> Supplier<out ItemLike>? = { variant ->
        RagiumItems.MATERIALS.get(variant, type)
    }

    @JvmStatic
    private fun gem(type: RagiumMaterialType): HTMaterialFamily = HTMaterialFamily.Builder
        .gem(RagiumItems.getGem(type))
        .setDefaultedEntry(HTMaterialVariant.DUST, getItem(type))
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, getBlock(type))
        .build(type.serializedName)

    @JvmStatic
    private fun ingot(type: RagiumMaterialType): HTMaterialFamily = HTMaterialFamily.Builder
        .ingot(RagiumItems.getIngot(type))
        .setDefaultedEntry(HTMaterialVariant.DUST, getItem(type))
        .setDefaultedEntry(HTMaterialVariant.NUGGET, getItem(type))
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, getBlock(type))
        .build(type.serializedName)

    @JvmStatic
    private fun ingotAlloy(type: RagiumMaterialType): HTMaterialFamily = HTMaterialFamily.Builder
        .ingotAlloy(RagiumItems.getIngot(type))
        .setDefaultedEntry(HTMaterialVariant.DUST, getItem(type))
        .setDefaultedEntry(HTMaterialVariant.NUGGET, getItem(type))
        .setDefaultedEntry(HTMaterialVariant.STORAGE_BLOCK, getBlock(type))
        .build(type.serializedName)
}
