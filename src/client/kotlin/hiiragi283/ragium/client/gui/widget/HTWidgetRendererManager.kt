package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.event.HTRegisterWidgetRendererEvent
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetRendererFactory
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.MenuScreens
import net.neoforged.fml.ModLoader

/**
 * 参照 : [Minecraft - MenuScreens][MenuScreens]
 */
internal object HTWidgetRendererManager {
    @JvmStatic
    private lateinit var factories: Map<HTWidgetType<*>, HTWidgetRendererFactory<*, *>>

    @JvmStatic
    fun init() {
        val map: MutableMap<HTWidgetType<*>, HTWidgetRendererFactory<*, *>> = hashMapOf()
        HTRegisterWidgetRendererEvent(map::put).let(ModLoader::postEvent)
        this.factories = map
        RagiumAPI.LOGGER.info("Initialized Widget Renderer Manager")
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <WIDGET : HTWidget> getFactory(type: HTWidgetType<WIDGET>): HTWidgetRendererFactory<WIDGET, *>? = factories[type] as? HTWidgetRendererFactory<WIDGET, *>

    @Suppress("UNCHECKED_CAST")
    fun <WIDGET : HTWidget> create(gui: HTGuiAccess, widget: WIDGET): Renderable? = getFactory(widget.getType() as HTWidgetType<WIDGET>)?.createRenderer(gui, widget)
}
