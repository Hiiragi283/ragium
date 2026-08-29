package hiiragi283.ragium.api.material.property

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI

/**
 * 参照 : [Hiiragi Core - HTMaterialPropertyKeys][hiiragi283.core.api.material.property.HTMaterialPropertyKeys]
 */
data object RagiumMaterialPropertyKeys {
    /**
     * @since 21.1.1.0
     */
    @JvmField
    val DEFAULT_FLUID_AMOUNT: HTPropertyKey<Int> = HTPropertyKey.create(RagiumAPI.id("default_fluid_amount"), HTConst.INGOT_AMOUNT)

    /**
     * @since 21.1.1.0
     */
    @JvmField
    val MELT_TO: HTPropertyKey<HTFluidContent?> = HTPropertyKey.createNullable(RagiumAPI.id("melt_to"))

    /**
     * @since 21.1.1.0
     */
    @JvmField
    val SOLIDIFY_FROM: HTPropertyKey<HTFluidContent?> = HTPropertyKey.createNullable(RagiumAPI.id("solidify_from"))

    @JvmField
    val MATTER_VALUE: HTPropertyKey<Int?> = HTPropertyKey.createNullable(RagiumAPI.id("matter_value"))
}
