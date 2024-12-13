package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey

/**
 * [HTPropertyKey] collection for materials
 * @see hiiragi283.ragium.api.RagiumPlugin.setupMaterialProperties
 */
object HTMaterialPropertyKeys {
    /**
     * Disable ingot/gem -> block crafting recipe
     *
     * Used in [hiiragi283.ragium.api.RagiumPlugin.registerRuntimeMaterialRecipes]
     */
    @JvmField
    val DISABLE_BLOCK_CRAFTING: HTPropertyKey.Defaulted<Unit> =
        HTPropertyKey.ofFlag(RagiumAPI.id("disable_block_crafting"))

    /**
     * Disable dust -> ingot smelting recipe
     *
     * Used in [hiiragi283.ragium.api.RagiumPlugin.registerRuntimeMaterialRecipes]
     */
    @JvmField
    val DISABLE_DUST_SMELTING: HTPropertyKey.Defaulted<Unit> =
        HTPropertyKey.ofFlag(RagiumAPI.id("disable_dust_smelting"))

    /**
     * Disable raw material -> ingot smelting recipe
     *
     * Used in [hiiragi283.ragium.api.RagiumPlugin.registerRuntimeMaterialRecipes]
     */
    @JvmField
    val DISABLE_RAW_SMELTING: HTPropertyKey.Defaulted<Unit> =
        HTPropertyKey.ofFlag(RagiumAPI.id("disable_raw_smelting"))

    /**
     * Provides output count for ore -> raw material/gem grinding recipe
     *
     * Actually, multiplied value is used
     * - Grinding Recipe -> 2x
     * - HCL Chemical Recipe -> 3x
     * - H2SO4 Chemical Recipe -> 4x
     * Used in [hiiragi283.ragium.api.RagiumPlugin.registerRuntimeMaterialRecipes]
     */
    @JvmField
    val GRINDING_BASE_COUNT: HTPropertyKey.Defaulted<Int> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("grinding_count"), 1)

    /**
     * Provides exp amount for smelting recipe
     *
     * Used in [hiiragi283.ragium.api.RagiumPlugin.registerRuntimeMaterialRecipes]
     */
    @JvmField
    val SMELTING_EXP: HTPropertyKey.Defaulted<Float> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("smelting_exp"), 0f)
}
