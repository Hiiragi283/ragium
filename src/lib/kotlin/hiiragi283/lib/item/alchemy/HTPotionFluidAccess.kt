package hiiragi283.lib.item.alchemy

import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI

interface HTPotionFluidAccess {
    companion object {
        @JvmField
        val INSTANCE: HTPotionFluidAccess = RagiumAPI.getService()
    }

    val fluidContent: HTFluidContent
}
