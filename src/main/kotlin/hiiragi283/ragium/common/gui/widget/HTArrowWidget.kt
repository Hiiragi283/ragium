package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType

sealed class HTArrowWidget(private val progressGetter: () -> Float, x: Int, y: Int) : HTAbstractWidget(x, y, 24, 16) {
    val progress: Float get() = progressGetter()

    override fun onInit(access: HTWidget.Access) {
        access.isActive = false
    }

    class Right(progressGetter: () -> Float, x: Int, y: Int) : HTArrowWidget(progressGetter, x, y) {
        override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.ARROW_RIGHT.get()

        override fun toString(): String = "HTProgressWidget.Right(bounds=$bounds, progress=$progress)"
    }

    class Left(progressGetter: () -> Float, x: Int, y: Int) : HTArrowWidget(progressGetter, x, y) {
        override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.ARROW_LEFT.get()

        override fun toString(): String = "HTProgressWidget.Left(bounds=$bounds, progress=$progress)"
    }
}
