package hiiragi283.ragium.common.material.part

import hiiragi283.core.api.material.part.HTDeferredPart
import hiiragi283.core.api.material.part.HTPartLike

/**
 * @see hiiragi283.core.common.plugin.CommonMaterialPlugin
 * @see hiiragi283.core.api.material.part.CommonParts
 */
data object RagiumParts {
    @JvmField
    val PELLET: HTPartLike = HTDeferredPart("pellet")
}
