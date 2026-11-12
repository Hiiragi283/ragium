package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.registry.HTDeferredHolder
import hiiragi283.lib.registry.HTDeferredRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries

data object RagiumWidgetTypes {
    @JvmField
    val REGISTER: HTDeferredRegister<HTWidgetType<*>> =
        HTDeferredRegister(RagiumRegistries.Keys.WIDGET_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <WIDGET : HTWidget> register(name: String): HTDeferredHolder<HTWidgetType<*>, HTWidgetType<WIDGET>> =
        REGISTER.register(name, HTWidgetType.Companion::simple)

    @JvmField
    val ENCHANTMENT: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTEnchantmentWidget>> = register(HTConstants.ENCHANTMENT)

    @JvmField
    val ENERGY: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTEnergySlotWidget>> = register(HTConstants.ENERGY)

    @JvmField
    val FLUID: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTFluidWidget>> = register(HTConstants.FLUID)

    @JvmField
    val ITEM: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTItemWidget>> = register(HTConstants.ITEM)

    @JvmField
    val PROGRESS: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTProgressWidget>> = register("progress")

    @JvmField
    val TEXTURE: HTDeferredHolder<HTWidgetType<*>, HTWidgetType<HTTextureWidget>> = register("texture")
}
