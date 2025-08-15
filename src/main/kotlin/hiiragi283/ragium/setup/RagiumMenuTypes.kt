package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.registry.HTMenuTypeRegister
import hiiragi283.ragium.common.inventory.HTAlloySmelterMenu
import hiiragi283.ragium.common.inventory.HTCrusherMenu
import hiiragi283.ragium.common.inventory.HTEnergyNetworkAccessMenu
import hiiragi283.ragium.common.inventory.HTEngraverMenu
import hiiragi283.ragium.common.inventory.HTFluidOnlyMenu
import hiiragi283.ragium.common.inventory.HTItemCollectorMenu
import hiiragi283.ragium.common.inventory.HTItemToItemMenu
import hiiragi283.ragium.common.inventory.HTItemWithFluidToItemMenu
import hiiragi283.ragium.common.inventory.HTMelterMenu
import hiiragi283.ragium.common.inventory.HTMixerMenu
import hiiragi283.ragium.common.inventory.HTSingleItemMenu
import hiiragi283.ragium.common.inventory.HTSlotConfigurationMenu

object RagiumMenuTypes {
    @JvmField
    val REGISTER = HTMenuTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val DRUM: HTDeferredMenuType<HTFluidOnlyMenu> = REGISTER.registerType("drum", HTFluidOnlyMenu::drum)

    @JvmField
    val SLOT_CONFIG: HTDeferredMenuType<HTSlotConfigurationMenu> =
        REGISTER.registerType("slot_configuration", ::HTSlotConfigurationMenu)

    //    Machine    //

    @JvmField
    val ALLOY_SMELTER: HTDeferredMenuType<HTAlloySmelterMenu> = REGISTER.registerType("alloy_smelter", ::HTAlloySmelterMenu)

    @JvmField
    val COMPRESSOR: HTDeferredMenuType<HTItemToItemMenu> = REGISTER.registerType("compressor", HTItemToItemMenu::compressor)

    @JvmField
    val CRUSHER: HTDeferredMenuType<HTCrusherMenu> = REGISTER.registerType("crusher", ::HTCrusherMenu)

    @JvmField
    val ENERGY_NETWORK_ACCESS: HTDeferredMenuType<HTEnergyNetworkAccessMenu> =
        REGISTER.registerType("energy_network_access", ::HTEnergyNetworkAccessMenu)

    @JvmField
    val ENGRAVER: HTDeferredMenuType<HTEngraverMenu> = REGISTER.registerType("engraver", ::HTEngraverMenu)

    @JvmField
    val EXTRACTOR: HTDeferredMenuType<HTItemToItemMenu> = REGISTER.registerType("extractor", HTItemToItemMenu::extractor)

    @JvmField
    val FLUID_COLLECTOR: HTDeferredMenuType<HTFluidOnlyMenu> = REGISTER.registerType("fluid_collector", HTFluidOnlyMenu::collector)

    @JvmField
    val INFUSER: HTDeferredMenuType<HTItemWithFluidToItemMenu> = REGISTER.registerType("infuser", HTItemWithFluidToItemMenu::infuser)

    @JvmField
    val ITEM_COLLECTOR: HTDeferredMenuType<HTItemCollectorMenu> = REGISTER.registerType("item_collector", ::HTItemCollectorMenu)

    @JvmField
    val MELTER: HTDeferredMenuType<HTMelterMenu> = REGISTER.registerType("melter", ::HTMelterMenu)

    @JvmField
    val MIXER: HTDeferredMenuType<HTMixerMenu> = REGISTER.registerType("mixer", ::HTMixerMenu)

    @JvmField
    val PULVERIZER: HTDeferredMenuType<HTItemToItemMenu> = REGISTER.registerType("pulverizer", HTItemToItemMenu::extractor)

    @JvmField
    val REFINERY: HTDeferredMenuType<HTFluidOnlyMenu> = REGISTER.registerType("refinery", HTFluidOnlyMenu::refinery)

    @JvmField
    val SINGLE_ITEM: HTDeferredMenuType<HTSingleItemMenu> = REGISTER.registerType("single_item", ::HTSingleItemMenu)

    @JvmField
    val SOLIDIFIER: HTDeferredMenuType<HTItemWithFluidToItemMenu> = REGISTER.registerType(
        "solidifier",
        HTItemWithFluidToItemMenu::solidifier,
    )
}
