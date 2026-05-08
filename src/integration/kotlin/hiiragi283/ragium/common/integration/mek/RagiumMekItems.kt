package hiiragi283.ragium.common.integration.mek

import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import hiiragi283.ragium.api.RagiumAPI

data object RagiumMekItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ENRICHED_RAGINITE: HTSimpleItemHolderLike = REGISTER.registerSimpleItem("enriched_raginite")
}
