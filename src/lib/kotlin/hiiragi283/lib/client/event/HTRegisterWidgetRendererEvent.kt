package hiiragi283.lib.client.event

import hiiragi283.lib.client.gui.widget.HTWidgetRendererFactory
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.client.gui.components.Renderable
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

/**
 * [HTWidgetType]と[Renderable]を紐づけるイベントクラスです。
 *
 * 参照 : [NeoForge - RegisterMenuScreensEvent][RegisterMenuScreensEvent]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTRegisterWidgetRendererEvent(private val registerer: (HTWidgetType<*>, HTWidgetRendererFactory<*, *>) -> HTWidgetRendererFactory<*, *>?) :
    Event(),
    IModBusEvent {
    /**
     * 指定した[type]と[factory]を紐づけます。
     * @param WIDGET [HTWidget]を実装したクラス
     * @throws IllegalStateException すでに[type]に[HTWidgetRendererFactory]が登録されている場合
     */
    fun <WIDGET : HTWidget, RENDERER : Renderable> register(
        type: HTWidgetType<WIDGET>,
        factory: HTWidgetRendererFactory<WIDGET, RENDERER>,
    ) {
        check(registerer(type, factory) == null) { "Duplicated widget renderer for ${RagiumRegistries.WIDGET_TYPE.getKey(type)}" }
    }
}
