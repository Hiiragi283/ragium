package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.text.Text
import hiiragi283.ragium.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.ragium.common.gui.menu.HTWidgetContainerMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent

/**
 * 参照 : [Mekanism - GuiElement](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/client/gui/element/GuiElement.java)
 */
class HTGuiWidget<WIDGET : HTWidget>(private val screen: HTWidgetContainerScreen, val widget: WIDGET) :
    AbstractWidget(
        widget.bounds.x + screen.getLeftPos(),
        widget.bounds.y + screen.getTopPos(),
        widget.bounds.width,
        widget.bounds.height,
        Text.empty(),
    ) {
    private val access = Access()
    private val menuAccess = MenuAccess()
    private val renderer: Renderable? by lazy { HTWidgetRendererManager.create(screen, widget) }

    init {
        widget.onInit(access)
    }

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        if (visible) {
            renderer?.extractRenderState(graphics, mouseX, mouseY, a)
        }
    }

    override fun onClick(event: MouseButtonEvent, doubleClick: Boolean) {
        widget.mouseClicked(menuAccess, event.x(), event.y(), event.button())
    }

    override fun onRelease(event: MouseButtonEvent) {
        widget.mouseReleased(event.x(), event.y())
    }

    override fun onDrag(event: MouseButtonEvent, dx: Double, dy: Double) {
        widget.mouseDragged(event.x(), event.y(), dx, dy)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean =
        widget.mouseScrolled(mouseX, mouseY, scrollX, scrollY)

    override fun keyPressed(event: KeyEvent): Boolean = widget.keyPressed(event.key(), event.scancode(), event.modifiers())

    override fun keyReleased(event: KeyEvent): Boolean = widget.keyReleased(event.key(), event.scancode(), event.modifiers())

    override fun charTyped(event: CharacterEvent): Boolean = widget.charTyped(event.codepointAsString()[0])

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {}

    private inner class Access : HTWidget.Access {
        override var isActive: Boolean by this@HTGuiWidget::active
        override var isVisible: Boolean by this@HTGuiWidget::visible
    }

    private inner class MenuAccess : HTWidget.MenuAccess {
        override val clientMenu: HTWidgetContainerMenu by this@HTGuiWidget.screen::menu
        override val widgetHolder: HTWidgetHolder by clientMenu::widgetHolder
    }
}
