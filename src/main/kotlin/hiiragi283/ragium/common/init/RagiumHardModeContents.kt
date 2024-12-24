package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTHardModeContent
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.item.Items

object RagiumHardModeContents {
    //    Gems    //

    @JvmField
    val DIAMOND: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.DIAMOND,
            Items.DIAMOND,
        ),
        RagiumItems.Plates.DIAMOND,
    )

    @JvmField
    val EMERALD: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.EMERALD,
            Items.EMERALD,
        ),
        RagiumItems.Plates.EMERALD,
    )

    @JvmField
    val LAPIS: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.LAPIS,
            Items.LAPIS_LAZULI,
        ),
        RagiumItems.Plates.LAPIS,
    )

    @JvmField
    val QUARTZ: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.QUARTZ,
            Items.QUARTZ,
        ),
        RagiumItems.Plates.QUARTZ,
    )

    //    Ingots    //

    @JvmField
    val ALUMINUM: HTHardModeContent = HTHardModeContent.of(
        RagiumItems.Ingots.ALUMINUM,
        RagiumItems.Plates.ALUMINUM,
    )

    @JvmField
    val COPPER: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.COPPER,
            Items.COPPER_INGOT,
        ),
        RagiumItems.Plates.COPPER,
    )

    @JvmField
    val DEEP_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumItems.Ingots.DEEP_STEEL,
        RagiumItems.Plates.DEEP_STEEL,
    )

    @JvmField
    val GOLD: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.GOLD,
            Items.GOLD_INGOT,
        ),
        RagiumItems.Plates.GOLD,
    )

    @JvmField
    val IRON: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.IRON,
            Items.IRON_INGOT,
        ),
        RagiumItems.Plates.IRON,
    )

    @JvmField
    val NETHERITE: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.NETHERITE,
            Items.NETHERITE_INGOT,
        ),
        RagiumItems.Plates.NETHERITE,
    )

    @JvmField
    val RAGI_ALLOY: HTHardModeContent = HTHardModeContent.of(
        RagiumItems.Ingots.RAGI_ALLOY,
        RagiumItems.Plates.RAGI_ALLOY,
    )

    @JvmField
    val RAGI_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumItems.Ingots.RAGI_STEEL,
        RagiumItems.Plates.RAGI_STEEL,
    )

    @JvmField
    val REFINED_RAGI_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumItems.Ingots.REFINED_RAGI_STEEL,
        RagiumItems.Plates.REFINED_RAGI_STEEL,
    )

    @JvmField
    val STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumItems.Ingots.STEEL,
        RagiumItems.Plates.STEEL,
    )
}
