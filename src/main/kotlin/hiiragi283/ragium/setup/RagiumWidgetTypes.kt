package hiiragi283.ragium.setup

import hiiragi283.core.common.registry.HTDeferredWidgetType
import hiiragi283.core.common.registry.register.HTDeferredWidgetTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget

object RagiumWidgetTypes {
    @JvmField
    val REGISTER = HTDeferredWidgetTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ENERGY_SLOT: HTDeferredWidgetType<HTEnergySlotWidget> = REGISTER.registerType("energy")
}
