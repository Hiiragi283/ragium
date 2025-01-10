package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTHardModeContent
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.item.Items

object RagiumHardModeContents {
    //    Gems    //

    @JvmField
    val DIAMOND: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.DIAMOND)
        .normal(HTTagPrefix.GEM, Items.DIAMOND)
        .hard(RagiumItems.Plates.DIAMOND)
        .build()

    @JvmField
    val EMERALD: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.EMERALD)
        .normal(HTTagPrefix.GEM, Items.EMERALD)
        .hard(RagiumItems.Plates.EMERALD)
        .build()

    @JvmField
    val LAPIS: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.LAPIS)
        .normal(HTTagPrefix.GEM, Items.LAPIS_LAZULI)
        .hard(RagiumItems.Plates.LAPIS)
        .build()

    @JvmField
    val QUARTZ: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.QUARTZ)
        .normal(HTTagPrefix.GEM, Items.QUARTZ)
        .hard(RagiumItems.Plates.QUARTZ)
        .build()

    //    Ingots    //

    @JvmField
    val ALUMINUM: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.ALUMINUM)
        .normal(RagiumItems.Ingots.ALUMINUM)
        .hard(RagiumItems.Plates.ALUMINUM)
        .build()

    @JvmField
    val COPPER: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.COPPER)
        .normal(HTTagPrefix.INGOT, Items.COPPER_INGOT)
        .hard(RagiumItems.Plates.COPPER)
        .build()

    @JvmField
    val DEEP_STEEL: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.DEEP_STEEL)
        .normal(RagiumItems.Ingots.DEEP_STEEL)
        .hard(RagiumItems.Plates.DEEP_STEEL)
        .build()

    @JvmField
    val GOLD: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.GOLD)
        .normal(HTTagPrefix.INGOT, Items.GOLD_INGOT)
        .hard(RagiumItems.Plates.GOLD)
        .build()

    @JvmField
    val IRON: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.IRON)
        .normal(HTTagPrefix.INGOT, Items.IRON_INGOT)
        .hard(RagiumItems.Plates.IRON)
        .build()

    @JvmField
    val NETHERITE: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.NETHERITE)
        .normal(HTTagPrefix.INGOT, Items.NETHERITE_INGOT)
        .hard(RagiumItems.Plates.NETHERITE)
        .build()

    @JvmField
    val RAGI_ALLOY: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.RAGI_ALLOY)
        .normal(RagiumItems.Ingots.RAGI_ALLOY)
        .hard(RagiumItems.Plates.RAGI_ALLOY)
        .build()

    @JvmField
    val RAGI_STEEL: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.RAGI_STEEL)
        .normal(RagiumItems.Ingots.RAGI_STEEL)
        .hard(RagiumItems.Plates.RAGI_STEEL)
        .build()

    @JvmField
    val REFINED_RAGI_STEEL: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.REFINED_RAGI_STEEL)
        .normal(RagiumItems.Ingots.REFINED_RAGI_STEEL)
        .hard(RagiumItems.Plates.REFINED_RAGI_STEEL)
        .build()

    @JvmField
    val STEEL: HTHardModeContent = HTHardModeContent
        .Builder(RagiumMaterialKeys.STEEL)
        .normal(RagiumItems.Ingots.STEEL)
        .hard(RagiumItems.Plates.STEEL)
        .build()
}
