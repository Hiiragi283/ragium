package hiiragi283.ragium.api.material.property

import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTFluidContent

fun HTPropertyMap.Builder.setMolten(content: HTFluidContent) {
    this[RagiumMaterialPropertyKeys.MELT_TO] = content
    this[RagiumMaterialPropertyKeys.SOLIDIFY_FROM] = content
}
