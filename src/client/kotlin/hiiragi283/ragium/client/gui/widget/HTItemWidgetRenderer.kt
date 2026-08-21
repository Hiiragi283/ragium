package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.gui.widget.HTItemWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.world.item.ItemStack

class HTItemWidgetRenderer(gui: HTGuiAccess, widget: HTItemWidget) : HTAbstractWidgetRenderer<HTItemWidget>(gui, widget) {
    override fun render(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Render background
        HTSpriteRenderHelper.blit(graphics, widget.backgroundType.slotTexture, bounds)
        // Render stack
        if (widget is HTItemWidget.Container) return
        val font: Font = Minecraft.getInstance().font
        val stack: ItemStack = widget.getStack()
        if (stack.isEmpty) return
        val (x: Int, y: Int) = bounds
        graphics.item(stack, x + 1, y + 1)
        graphics.itemDecorations(font, stack, x + 1, y + 1)
        // Render tooltip
        if (bounds.contains(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(font, stack, mouseX, mouseY)
        }
    }
}
