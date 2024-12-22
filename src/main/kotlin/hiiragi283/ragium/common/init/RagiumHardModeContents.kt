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
        RagiumItemsNew.Plates.DIAMOND,
    )

    @JvmField
    val EMERALD: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.EMERALD,
            Items.EMERALD,
        ),
        RagiumItemsNew.Plates.EMERALD,
    )

    @JvmField
    val LAPIS: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.LAPIS,
            Items.LAPIS_LAZULI,
        ),
        RagiumItemsNew.Plates.LAPIS,
    )

    @JvmField
    val QUARTZ: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.QUARTZ,
            Items.QUARTZ,
        ),
        RagiumItemsNew.Plates.QUARTZ,
    )

    //    Ingots    //

    @JvmField
    val ALUMINUM: HTHardModeContent = HTHardModeContent.of(
        RagiumItemsNew.Ingots.ALUMINUM,
        RagiumItemsNew.Plates.ALUMINUM,
    )

    @JvmField
    val COPPER: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.COPPER,
            Items.COPPER_INGOT,
        ),
        RagiumItemsNew.Plates.COPPER,
    )

    @JvmField
    val DEEP_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumItemsNew.Ingots.DEEP_STEEL,
        RagiumItemsNew.Plates.DEEP_STEEL,
    )

    @JvmField
    val GOLD: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.GOLD,
            Items.GOLD_INGOT,
        ),
        RagiumItemsNew.Plates.GOLD,
    )

    @JvmField
    val IRON: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.IRON,
            Items.IRON_INGOT,
        ),
        RagiumItemsNew.Plates.IRON,
    )

    @JvmField
    val NETHERITE: HTHardModeContent = HTHardModeContent.of(
        HTMaterialProvider.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.NETHERITE,
            Items.NETHERITE_INGOT,
        ),
        RagiumItemsNew.Plates.NETHERITE,
    )

    @JvmField
    val RAGI_ALLOY: HTHardModeContent = HTHardModeContent.of(
        RagiumItemsNew.Ingots.RAGI_ALLOY,
        RagiumItemsNew.Plates.RAGI_ALLOY,
    )

    @JvmField
    val RAGI_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumItemsNew.Ingots.RAGI_STEEL,
        RagiumItemsNew.Plates.RAGI_STEEL,
    )

    @JvmField
    val REFINED_RAGI_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumItemsNew.Ingots.REFINED_RAGI_STEEL,
        RagiumItemsNew.Plates.REFINED_RAGI_STEEL,
    )

    @JvmField
    val STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumItemsNew.Ingots.STEEL,
        RagiumItemsNew.Plates.STEEL,
    )
}
