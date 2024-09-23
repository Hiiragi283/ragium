package hiiragi283.ragium.data.util

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.item.Items

object RagiumMetalItemRecipeGroups {
    @JvmField
    val IRON: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(Items.IRON_BLOCK, true)
            .ingot(Items.IRON_INGOT, true)
            .ore(Items.IRON_ORE)
            .rawMaterial(Items.RAW_IRON)
            .build("iron")

    @JvmField
    val GOLD: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(Items.GOLD_BLOCK, true)
            .ingot(Items.GOLD_INGOT, true)
            .ore(Items.GOLD_ORE)
            .rawMaterial(Items.RAW_GOLD)
            .build("gold")

    @JvmField
    val COPPER: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(Items.COPPER_BLOCK, true)
            .ingot(Items.COPPER_INGOT, true)
            .ore(Items.COPPER_ORE)
            .rawMaterial(Items.RAW_COPPER)
            .build("copper")

    @JvmField
    val NETHERITE: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(Items::NETHERITE_BLOCK, true)
            .ingot(Items::NETHERITE_INGOT, true)
            .build("netherite")

    @JvmField
    val RAGINITE: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .dust(RagiumItems.Dusts.RAW_RAGINITE)
            .ore(RagiumContents.RAGINITE_ORE)
            .rawMaterial(RagiumItems.RAW_RAGINITE)
            .build("raginite")

    @JvmField
    val RAGI_ALLOY: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumContents.StorageBlocks.RAGI_ALLOY)
            .ingot(RagiumItems.Ingots.RAGI_ALLOY)
            .plate(RagiumItems.Plates.RAGI_ALLOY)
            .build("ragi_alloy")

    @JvmField
    val RAGI_STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumContents.StorageBlocks.RAGI_STEEL)
            .ingot(RagiumItems.Ingots.RAGI_STEEL)
            .plate(RagiumItems.Plates.RAGI_STEEL)
            .build("ragi_steel")

    @JvmField
    val REFINED_RAGI_STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumContents.StorageBlocks.REFINED_RAGI_STEEL)
            .ingot(RagiumItems.Ingots.REFINED_RAGI_STEEL)
            .plate(RagiumItems.Plates.REFINED_RAGI_STEEL)
            .build("refined_ragi_steel")

    @JvmField
    val STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .ingot(RagiumItems.Ingots.STEEL)
            .plate(RagiumItems.Plates.STEEL)
            .build("steel")

    @JvmField
    val TWILIGHT_METAL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .ingot(RagiumItems.Ingots.TWILIGHT_METAL)
            .plate(RagiumItems.Plates.TWILIGHT)
            .build("twilight_metal")
}
