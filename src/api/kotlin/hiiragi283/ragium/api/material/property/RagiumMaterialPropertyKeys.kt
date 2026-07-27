package hiiragi283.ragium.api.material.property

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI

/**
 * @see hiiragi283.core.api.material.property.HTMaterialPropertyKeys
 */
data object RagiumMaterialPropertyKeys {
    @JvmField
    val DEFAULT_FLUID_AMOUNT: HTPropertyKey<Int> = HTPropertyKey.create(RagiumAPI.id("default_fluid_amount"), HTConst.INGOT_AMOUNT)

    @JvmField
    val MOLTEN_FLUID: HTPropertyKey<HTFluidContent?> = HTPropertyKey.createNullable(RagiumAPI.id("molten_fluid"))

    @JvmField
    val MATTER_VALUE: HTPropertyKey<Int?> = HTPropertyKey.createNullable(RagiumAPI.id("matter_value"))
}
