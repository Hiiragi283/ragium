package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTHardModeContent
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.item.Items

object RagiumHardModeContents {
    //    Gems    //

    @JvmField
    val DIAMOND: HTHardModeContent = HTHardModeContent.of(
        HTContent.Material.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.DIAMOND,
            Items.DIAMOND,
        ),
        RagiumContents.Plates.DIAMOND,
    )

    @JvmField
    val EMERALD: HTHardModeContent = HTHardModeContent.of(
        HTContent.Material.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.EMERALD,
            Items.EMERALD,
        ),
        RagiumContents.Plates.EMERALD,
    )

    @JvmField
    val LAPIS: HTHardModeContent = HTHardModeContent.of(
        HTContent.Material.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.LAPIS,
            Items.LAPIS_LAZULI,
        ),
        RagiumContents.Plates.LAPIS,
    )

    @JvmField
    val QUARTZ: HTHardModeContent = HTHardModeContent.of(
        HTContent.Material.ofWrapped(
            HTTagPrefix.GEM,
            RagiumMaterialKeys.QUARTZ,
            Items.QUARTZ,
        ),
        RagiumContents.Plates.QUARTZ,
    )

    //    Ingots    //

    @JvmField
    val ALUMINUM: HTHardModeContent = HTHardModeContent.of(
        RagiumContents.Ingots.ALUMINUM,
        RagiumContents.Plates.ALUMINUM,
    )

    @JvmField
    val COPPER: HTHardModeContent = HTHardModeContent.of(
        HTContent.Material.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.COPPER,
            Items.COPPER_INGOT,
        ),
        RagiumContents.Plates.COPPER,
    )

    @JvmField
    val DEEP_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumContents.Ingots.DEEP_STEEL,
        RagiumContents.Plates.DEEP_STEEL,
    )

    @JvmField
    val GOLD: HTHardModeContent = HTHardModeContent.of(
        HTContent.Material.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.GOLD,
            Items.GOLD_INGOT,
        ),
        RagiumContents.Plates.GOLD,
    )

    @JvmField
    val IRON: HTHardModeContent = HTHardModeContent.of(
        HTContent.Material.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.IRON,
            Items.IRON_INGOT,
        ),
        RagiumContents.Plates.IRON,
    )

    @JvmField
    val NETHERITE: HTHardModeContent = HTHardModeContent.of(
        HTContent.Material.ofWrapped(
            HTTagPrefix.INGOT,
            RagiumMaterialKeys.NETHERITE,
            Items.NETHERITE_INGOT,
        ),
        RagiumContents.Plates.NETHERITE,
    )

    @JvmField
    val RAGI_ALLOY: HTHardModeContent = HTHardModeContent.of(
        RagiumContents.Ingots.RAGI_ALLOY,
        RagiumContents.Plates.RAGI_ALLOY,
    )

    @JvmField
    val RAGI_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumContents.Ingots.RAGI_STEEL,
        RagiumContents.Plates.RAGI_STEEL,
    )

    @JvmField
    val REFINED_RAGI_STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumContents.Ingots.REFINED_RAGI_STEEL,
        RagiumContents.Plates.REFINED_RAGI_STEEL,
    )

    @JvmField
    val STEEL: HTHardModeContent = HTHardModeContent.of(
        RagiumContents.Ingots.STEEL,
        RagiumContents.Plates.STEEL,
    )
}
