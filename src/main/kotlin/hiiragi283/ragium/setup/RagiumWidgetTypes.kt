package hiiragi283.ragium.setup

import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.registry.HTDeferredWidgetTypeRegister
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget

object RagiumWidgetTypes {
    @JvmField
    val REGISTER = HTDeferredWidgetTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ENERGY_SLOT: SupplierWithId<HTWidgetType<HTEnergySlotWidget>> = REGISTER.registerType<HTEnergySlotWidget>("energy")
}
