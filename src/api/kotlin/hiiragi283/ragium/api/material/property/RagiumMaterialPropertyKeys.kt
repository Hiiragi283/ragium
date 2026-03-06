package hiiragi283.ragium.api.material.property

import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.ragium.api.RagiumAPI

/**
 * @see hiiragi283.core.api.material.property.HTMaterialPropertyKeys
 */
object RagiumMaterialPropertyKeys {
    @JvmField
    val MATTER_VALUE: HTPropertyKey<Int?> = HTPropertyKey.createNullable(RagiumAPI.id("matter_value"))
}
