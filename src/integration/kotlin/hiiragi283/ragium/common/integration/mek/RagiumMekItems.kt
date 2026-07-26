package hiiragi283.ragium.common.integration.mek

import hiiragi283.core.api.registry.HTDeferredItemRegister
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.RagiumAPI

data object RagiumMekItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ENRICHED_RAGINITE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("enriched_raginite")
}
