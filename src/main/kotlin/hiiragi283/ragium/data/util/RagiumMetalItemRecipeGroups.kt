package hiiragi283.ragium.data.util

import hiiragi283.ragium.common.RagiumContents
import net.minecraft.item.Items

object RagiumMetalItemRecipeGroups {
    @JvmField
    val IRON: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(Items.IRON_BLOCK, true)
            .ingot(Items.IRON_INGOT, true)
            .ore(Items.IRON_ORE)
            .plate(RagiumContents.Plates.IRON)
            .rawMaterial(Items.RAW_IRON)
            .build("iron")

    @JvmField
    val GOLD: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(Items.GOLD_BLOCK, true)
            .ingot(Items.GOLD_INGOT, true)
            .ore(Items.GOLD_ORE)
            .plate(RagiumContents.Plates.GOLD)
            .rawMaterial(Items.RAW_GOLD)
            .build("gold")

    @JvmField
    val COPPER: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(Items.COPPER_BLOCK, true)
            .ingot(Items.COPPER_INGOT, true)
            .ore(Items.COPPER_ORE)
            .plate(RagiumContents.Plates.COPPER)
            .rawMaterial(Items.RAW_COPPER)
            .build("copper")

    @JvmField
    val NETHERITE: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(Items::NETHERITE_BLOCK, true)
            .ingot(Items::NETHERITE_INGOT, true)
            .plate(RagiumContents.Plates.NETHERITE)
            .build("netherite")

    @JvmField
    val RAGINITE: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .dust(RagiumContents.Dusts.RAW_RAGINITE)
            .ore(RagiumContents.RAGINITE_ORE)
            .rawMaterial(RagiumContents.RAW_RAGINITE)
            .build("raginite")

    @JvmField
    val RAGI_ALLOY: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumContents.StorageBlocks.RAGI_ALLOY)
            .ingot(RagiumContents.Ingots.RAGI_ALLOY)
            .plate(RagiumContents.Plates.RAGI_ALLOY)
            .build("ragi_alloy")

    @JvmField
    val RAGI_STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumContents.StorageBlocks.RAGI_STEEL)
            .ingot(RagiumContents.Ingots.RAGI_STEEL)
            .plate(RagiumContents.Plates.RAGI_STEEL)
            .build("ragi_steel")

    @JvmField
    val REFINED_RAGI_STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumContents.StorageBlocks.REFINED_RAGI_STEEL)
            .ingot(RagiumContents.Ingots.REFINED_RAGI_STEEL)
            .plate(RagiumContents.Plates.REFINED_RAGI_STEEL)
            .build("refined_ragi_steel")

    @JvmField
    val STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .ingot(RagiumContents.Ingots.STEEL)
            .plate(RagiumContents.Plates.STEEL)
            .build("steel")

    @JvmField
    val TWILIGHT_METAL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .ingot(RagiumContents.Ingots.TWILIGHT_METAL)
            .plate(RagiumContents.Plates.TWILIGHT)
            .build("twilight_metal")
}
