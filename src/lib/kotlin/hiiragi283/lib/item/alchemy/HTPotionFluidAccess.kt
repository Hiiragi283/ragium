package hiiragi283.lib.item.alchemy

import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.component.DataComponentType

interface HTPotionFluidAccess {
    companion object {
        @JvmField
        val INSTANCE: HTPotionFluidAccess = RagiumAPI.getService()
    }

    val fluidContent: HTFluidContent
    val bottleType: DataComponentType<HTBottleType>
}
