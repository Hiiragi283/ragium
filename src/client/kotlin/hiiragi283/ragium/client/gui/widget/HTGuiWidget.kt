package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.text.Text
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.world.item.ItemStack

/**
 * 参照 : [Mekanism - GuiElement](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/gui/element/GuiElement.java)
 */
class HTGuiWidget<WIDGET : HTWidget>(private val gui: HTGuiAccess, val widget: WIDGET) :
    AbstractWidget(
        widget.bounds.x + gui.getGuiLeft(),
        widget.bounds.y + gui.getGuiTop(),
        widget.bounds.width,
        widget.bounds.height,
        Text.empty()
    ) {
    private val access = Access()
    private val renderer: Renderable? by lazy { HTWidgetRendererManager.create(gui, widget) }

    init {
        widget.onInit(access)
    }

    override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        if (visible) {
            renderer?.extractRenderState(graphics, mouseX, mouseY, a)
        }
    }

    override fun onClick(event: MouseButtonEvent, doubleClick: Boolean) {
        widget.mouseClicked(access, event.x(), event.y(), event.button())
    }

    override fun onRelease(event: MouseButtonEvent) {
        widget.mouseReleased(event.x(), event.y())
    }

    override fun onDrag(event: MouseButtonEvent, dx: Double, dy: Double) {
        widget.mouseDragged(event.x(), event.y(), dx, dy)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean =
        widget.mouseScrolled(mouseX, mouseY, scrollX, scrollY)

    override fun keyPressed(event: KeyEvent): Boolean =
        widget.keyPressed(event.key(), event.scancode(), event.modifiers())

    override fun keyReleased(event: KeyEvent): Boolean =
        widget.keyReleased(event.key(), event.scancode(), event.modifiers())

    override fun charTyped(event: CharacterEvent): Boolean = widget.charTyped(event.codepointAsString()[0])

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {}

    //    HTWidget.Access    //

    private inner class Access : HTWidget.Access {
        override var isActive: Boolean by this@HTGuiWidget::active
        override var isVisible: Boolean by this@HTGuiWidget::visible
        override val carried: ItemStack by this@HTGuiWidget.gui::carried
    }
}
