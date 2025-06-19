package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.registry.HTMenuTypeRegister
import hiiragi283.ragium.common.inventory.HTAlloySmelterMenu
import hiiragi283.ragium.common.inventory.HTCrusherMenu

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTMenuTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ALLOY_SMELTER: HTDeferredMenuType<HTAlloySmelterMenu> = REGISTER.registerType("alloy_smelter", ::HTAlloySmelterMenu)

    @JvmField
    val CRUSHER: HTDeferredMenuType<HTCrusherMenu> = REGISTER.registerType("crusher", ::HTCrusherMenu)
}
