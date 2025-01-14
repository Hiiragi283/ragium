package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import java.util.function.Function

object DefaultMaterialPlugin : RagiumPlugin {
    override val priority: Int = -100

    override fun setupMaterialProperties(helper: Function<HTMaterialKey, HTPropertyHolderBuilder>) {
        // metal
        helper
            .apply(RagiumMaterialKeys.COPPER)
            .add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
            .put(HTMaterialPropertyKeys.SMELTING_EXP, 0.7f)

        helper
            .apply(RagiumMaterialKeys.GOLD)
            .add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
            .put(HTMaterialPropertyKeys.SMELTING_EXP, 1f)

        helper
            .apply(RagiumMaterialKeys.IRON)
            .add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
            .put(HTMaterialPropertyKeys.SMELTING_EXP, 0.7f)

        helper.apply(RagiumMaterialKeys.IRIDIUM).add(
            HTMaterialPropertyKeys.DISABLE_DUST_SMELTING,
            HTMaterialPropertyKeys.DISABLE_RAW_SMELTING,
        )

        helper.apply(RagiumMaterialKeys.TITANIUM).add(
            HTMaterialPropertyKeys.DISABLE_DUST_SMELTING,
            HTMaterialPropertyKeys.DISABLE_RAW_SMELTING,
        )

        helper.apply(RagiumMaterialKeys.TUNGSTEN).add(
            HTMaterialPropertyKeys.DISABLE_DUST_SMELTING,
            HTMaterialPropertyKeys.DISABLE_RAW_SMELTING,
        )

        helper.apply(RagiumMaterialKeys.URANIUM).add(
            HTMaterialPropertyKeys.DISABLE_DUST_SMELTING,
            HTMaterialPropertyKeys.DISABLE_RAW_SMELTING,
        )
        // gem
        helper.apply(RagiumMaterialKeys.AMETHYST).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)

        helper.apply(RagiumMaterialKeys.COAL).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)

        helper.apply(RagiumMaterialKeys.EMERALD).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)

        helper.apply(RagiumMaterialKeys.DIAMOND).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)

        helper
            .apply(RagiumMaterialKeys.LAPIS)
            .add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
            .put(HTMaterialPropertyKeys.GRINDING_BASE_COUNT, 4)
        helper.apply(RagiumMaterialKeys.QUARTZ).add(HTMaterialPropertyKeys.DISABLE_BLOCK_CRAFTING)
        // mineral
        helper
            .apply(RagiumMaterialKeys.REDSTONE)
            .put(HTMaterialPropertyKeys.GRINDING_BASE_COUNT, 2)
            .put(HTMaterialPropertyKeys.ORE_SUB_PRODUCT, RagiumItems.Gems.CINNABAR)
    }
}
