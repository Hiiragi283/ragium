package hiiragi283.ragium.setup

import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget

data object RagiumWidgetTypes {
    @JvmField
    val ENERGY_SLOT: HTWidgetType.Simple<HTEnergySlotWidget> = HTWidgetType.Simple(RagiumAPI.id("energy"))
}
