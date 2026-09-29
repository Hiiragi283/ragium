package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTGuiRenderHelper
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.lib.text.Text
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.resources.Identifier

class HTFluidWidgetRenderer(gui: HTGuiAccess, widget: HTFluidWidget) :
    HTAbstractWidgetRenderer<HTFluidWidget>(gui, widget) {
    override fun render(
        bounds: HTBounds,
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        // Render background
        val background: Identifier = when (widget) {
            is HTFluidWidget.Slot -> widget.backgroundType.slotTexture
            is HTFluidWidget.Tank -> widget.backgroundType.tankTexture
        }
        HTSpriteRenderHelper.blit(graphics, background, bounds)
        // Render sprite
        var (x: Int, y: Int, width: Int, height: Int) = bounds
        x++
        y++
        width -= 2
        height -= 2
        HTGuiRenderHelper.renderFluid(graphics, widget.getFluidStack(), x, y, width, height)
        // Render tooltip
        if (bounds.contains(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(
                Minecraft.getInstance().font,
                buildList {
                    HTStorageHelper.addFluidTooltip(
                        widget.getFluidStack(),
                        { text: Text -> this.add(text.visualOrderText) },
                        HTGuiRenderHelper.getTooltipContext(),
                        HTGuiRenderHelper.getTooltipFlag(),
                        false
                    )
                },
                mouseX,
                mouseY
            )
        }
    }
}
