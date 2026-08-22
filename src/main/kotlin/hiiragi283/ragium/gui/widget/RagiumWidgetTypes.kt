package hiiragi283.ragium.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.ragium.api.RagiumAPI

data object RagiumWidgetTypes {
    @JvmStatic
    val allTypes: Set<HTWidgetType<*>> field: MutableSet<HTWidgetType<*>> = mutableSetOf()

    @JvmStatic
    private fun <T : HTWidget> create(name: String): HTWidgetType<T> = HTWidgetType<T>(RagiumAPI.id(name)).also(allTypes::add)

    @JvmField
    val ENERGY: HTWidgetType<HTEnergySlotWidget> = create(HTConstants.ENERGY)

    @JvmField
    val FLUID: HTWidgetType<HTFluidWidget> = create(HTConstants.FLUID)

    @JvmField
    val ITEM: HTWidgetType<HTItemWidget> = create(HTConstants.ITEM)

    @JvmField
    val PROGRESS: HTWidgetType<HTProgressWidget> = create("progress")
}
