package hiiragi283.ragium.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.Identifier

class HTProgressWidget : HTAbstractWidget {
    companion object {
        @JvmStatic
        fun createArrow(progressGetter: () -> Float, x: Int, y: Int): HTProgressWidget = HTProgressWidget(
            progressGetter,
            x,
            y,
            24,
            16,
        ).setTexture(RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, "arrow"))
    }

    private val progressGetter: () -> Float

    constructor(progressGetter: () -> Float, bounds: HTBounds) : super(bounds) {
        this.progressGetter = progressGetter
    }

    constructor(progressGetter: () -> Float, x: Int, y: Int, width: Int, height: Int) : super(x, y, width, height) {
        this.progressGetter = progressGetter
    }

    var texture: Identifier? = null
    val backgroundTexture: Identifier? get() = texture?.withSuffix("_background")

    fun setTexture(texture: Identifier): HTProgressWidget = apply { this.texture = texture }

    var fillDirection = HTFillDirection.LEFT_TO_RIGHT

    fun setDirection(direction: HTFillDirection): HTProgressWidget = apply { this.fillDirection = direction }

    fun getProgress(): Float = progressGetter()

    override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.PROGRESS

    override fun toString(): String = "HTProgressWidget(bounds=$bounds, progress=${getProgress()}, fillDirection=$fillDirection)"
}
