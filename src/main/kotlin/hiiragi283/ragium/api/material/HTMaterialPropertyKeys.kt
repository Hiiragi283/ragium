package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey

object HTMaterialPropertyKeys {
    @JvmField
    val GRINDER_RAW_COUNT: HTPropertyKey<Int> = HTPropertyKey.withDefault(RagiumAPI.id("grinder_raw_count"), 1)
}
