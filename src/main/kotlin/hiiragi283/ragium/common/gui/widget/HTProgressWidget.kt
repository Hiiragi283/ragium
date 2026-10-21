package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType

class HTProgressWidget(private val progressGetter: () -> Float, x: Int, y: Int) : HTAbstractWidget(x, y, 24, 16) {
    val progress: Float get() = progressGetter()

    override fun onInit(access: HTWidget.Access) {
        access.isActive = false
    }

    override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.PROGRESS.get()

    override fun toString(): String = "HTProgressWidget(bounds=$bounds, progress=$progress)"
}
