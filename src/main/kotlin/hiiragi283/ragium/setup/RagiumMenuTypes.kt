package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.registry.HTMenuTypeRegister
import hiiragi283.ragium.common.inventory.HTAlloySmelterMenu
import hiiragi283.ragium.common.inventory.HTBlockBreakerMenu
import hiiragi283.ragium.common.inventory.HTCrusherMenu
import hiiragi283.ragium.common.inventory.HTEnergyNetworkAccessMenu
import hiiragi283.ragium.common.inventory.HTExtractorMenu
import hiiragi283.ragium.common.inventory.HTFluidCollectorMenu
import hiiragi283.ragium.common.inventory.HTItemCollectorMenu
import hiiragi283.ragium.common.inventory.HTMelterMenu

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTMenuTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ALLOY_SMELTER: HTDeferredMenuType<HTAlloySmelterMenu> = REGISTER.registerType("alloy_smelter", ::HTAlloySmelterMenu)

    @JvmField
    val BLOCK_BREAKER: HTDeferredMenuType<HTBlockBreakerMenu> = REGISTER.registerType("block_breaker", ::HTBlockBreakerMenu)

    @JvmField
    val CRUSHER: HTDeferredMenuType<HTCrusherMenu> = REGISTER.registerType("crusher", ::HTCrusherMenu)

    @JvmField
    val ENERGY_NETWORK_ACCESS: HTDeferredMenuType<HTEnergyNetworkAccessMenu> =
        REGISTER.registerType("energy_network_access", ::HTEnergyNetworkAccessMenu)

    @JvmField
    val EXTRACTOR: HTDeferredMenuType<HTExtractorMenu> = REGISTER.registerType("extractor", ::HTExtractorMenu)

    @JvmField
    val FLUID_COLLECTOR: HTDeferredMenuType<HTFluidCollectorMenu> = REGISTER.registerType("fluid_collector", ::HTFluidCollectorMenu)

    @JvmField
    val ITEM_COLLECTOR: HTDeferredMenuType<HTItemCollectorMenu> = REGISTER.registerType("item_collector", ::HTItemCollectorMenu)

    @JvmField
    val MELTER: HTDeferredMenuType<HTMelterMenu> = REGISTER.registerType("melter", ::HTMelterMenu)
}
