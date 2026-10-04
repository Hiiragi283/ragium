package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.registry.HTDeferredHolder
import hiiragi283.lib.registry.HTDeferredRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries

data object RagiumWidgetTypes {
    @JvmField
    val REGISTER: HTDeferredRegister<HTWidgetType<*>> =
        HTDeferredRegister(RagiumRegistries.Keys.WIDGET_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val ENERGY: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTEnergySlotWidget>> =
        REGISTER.register(HTConstants.ENERGY, HTWidgetType.Companion::simple)

    @JvmField
    val FLUID: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTFluidWidget>> =
        REGISTER.register(HTConstants.FLUID, HTWidgetType.Companion::simple)

    @JvmField
    val ITEM: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTItemWidget>> =
        REGISTER.register(HTConstants.ITEM, HTWidgetType.Companion::simple)

    @JvmField
    val PROGRESS: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTProgressWidget>> =
        REGISTER.register("progress", HTWidgetType.Companion::simple)
}
