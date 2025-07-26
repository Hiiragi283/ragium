package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.registry.HTMenuTypeRegister
import hiiragi283.ragium.common.inventory.HTBlockBreakerMenu
import hiiragi283.ragium.common.inventory.HTCombineProcessMenu
import hiiragi283.ragium.common.inventory.HTDecomposeProcessMenu
import hiiragi283.ragium.common.inventory.HTEnergyNetworkAccessMenu
import hiiragi283.ragium.common.inventory.HTFluidCollectorMenu
import hiiragi283.ragium.common.inventory.HTItemCollectorMenu
import hiiragi283.ragium.common.inventory.HTMelterMenu
import hiiragi283.ragium.common.inventory.HTSingleProcessMenu
import hiiragi283.ragium.common.inventory.HTSolidifierMenu

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTMenuTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ALLOY_SMELTER: HTDeferredMenuType<HTCombineProcessMenu> = REGISTER.registerType("alloy_smelter", HTCombineProcessMenu::alloy)

    @JvmField
    val BLOCK_BREAKER: HTDeferredMenuType<HTBlockBreakerMenu> = REGISTER.registerType("block_breaker", ::HTBlockBreakerMenu)

    @JvmField
    val CRUSHER: HTDeferredMenuType<HTDecomposeProcessMenu> = REGISTER.registerType("crusher", HTDecomposeProcessMenu::crusher)

    @JvmField
    val ENERGY_NETWORK_ACCESS: HTDeferredMenuType<HTEnergyNetworkAccessMenu> =
        REGISTER.registerType("energy_network_access", ::HTEnergyNetworkAccessMenu)

    @JvmField
    val EXTRACTOR: HTDeferredMenuType<HTDecomposeProcessMenu> = REGISTER.registerType("extractor", HTDecomposeProcessMenu::extractor)

    @JvmField
    val FLUID_COLLECTOR: HTDeferredMenuType<HTFluidCollectorMenu> = REGISTER.registerType("fluid_collector", ::HTFluidCollectorMenu)

    @JvmField
    val FORMING_PRESS: HTDeferredMenuType<HTCombineProcessMenu> = REGISTER.registerType("forming_press", HTCombineProcessMenu::press)

    @JvmField
    val INFUSER: HTDeferredMenuType<HTSingleProcessMenu> = REGISTER.registerType("infuser", HTSingleProcessMenu::infuser)

    @JvmField
    val ITEM_COLLECTOR: HTDeferredMenuType<HTItemCollectorMenu> = REGISTER.registerType("item_collector", ::HTItemCollectorMenu)

    @JvmField
    val MELTER: HTDeferredMenuType<HTMelterMenu> = REGISTER.registerType("melter", ::HTMelterMenu)

    @JvmField
    val SOLIDIFIER: HTDeferredMenuType<HTSolidifierMenu> = REGISTER.registerType("solidifier", ::HTSolidifierMenu)
}
