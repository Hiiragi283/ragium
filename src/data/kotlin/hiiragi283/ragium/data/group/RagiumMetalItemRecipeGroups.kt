package hiiragi283.ragium.data.group

import hiiragi283.ragium.common.init.RagiumBlocks
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
            .dust(RagiumItems.RAW_RAGINITE_DUST)
            .ore(RagiumBlocks.RAGINITE_ORE)
            .rawMaterial(RagiumItems.RAW_RAGINITE)
            .build("raginite")

    @JvmField
    val RAGI_ALLOY: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumBlocks.RAGI_ALLOY_BLOCK)
            .ingot(RagiumItems.RAGI_ALLOY_INGOT)
            .plate(RagiumItems.RAGI_ALLOY_PLATE)
            .build("ragi_alloy")

    @JvmField
    val RAGI_STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumBlocks.RAGI_STEEL_BLOCK)
            .ingot(RagiumItems.RAGI_STEEL_INGOT)
            .plate(RagiumItems.RAGI_STEEL_PLATE)
            .build("ragi_steel")

    @JvmField
    val REFINED_RAGI_STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .block(RagiumBlocks.REFINED_RAGI_STEEL_BLOCK)
            .ingot(RagiumItems.REFINED_RAGI_STEEL_INGOT)
            .plate(RagiumItems.REFINED_RAGI_STEEL_PLATE)
            .build("refined_ragi_steel")

    @JvmField
    val STEEL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .ingot(RagiumItems.STEEL_INGOT)
            .build("steel")

    @JvmField
    val TWILIGHT_METAL: HTMetalItemRecipeGroup =
        HTMetalItemRecipeGroup
            .Builder()
            .ingot(RagiumItems.TWILIGHT_METAL_INGOT)
            .build("twilight_metal")
}
