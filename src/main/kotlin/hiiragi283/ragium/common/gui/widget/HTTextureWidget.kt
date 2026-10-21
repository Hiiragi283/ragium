package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import net.minecraft.resources.Identifier

class HTTextureWidget : HTAbstractWidget {
    val textureId: Identifier

    constructor(textureId: Identifier, x: Int, y: Int, width: Int, height: Int) : super(x, y, width, height) {
        this.textureId = textureId
    }

    constructor(textureId: Identifier, bounds: HTBounds) : super(bounds) {
        this.textureId = textureId
    }

    override fun onInit(access: HTWidget.Access) {
        access.isActive = false
    }

    override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.TEXTURE.get()

    override fun equals(other: Any?): Boolean = other is HTTextureWidget && other.textureId == this.textureId

    override fun hashCode(): Int = textureId.hashCode()

    override fun toString(): String = "HTTextureWidget(textureId=$textureId)"
}
