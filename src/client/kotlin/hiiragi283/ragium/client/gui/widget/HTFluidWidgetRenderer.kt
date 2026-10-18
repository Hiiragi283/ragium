package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTGuiRenderHelper
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.SpriteContents
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

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
        HTSpriteRenderHelper.blit(
            graphics,
            when (widget) {
                is HTFluidWidget.Slot -> widget.backgroundType.slotTexture
                is HTFluidWidget.Tank -> widget.backgroundType.tankTexture
            },
            bounds
        )
        // Render sprite
        renderFluid(bounds, graphics)
        // Render tooltip
        renderTooltips(bounds, graphics, mouseX, mouseY) { context: Item.TooltipContext, flag: TooltipFlag ->
            buildList { HTStorageHelper.addFluidTooltip(widget.getFluidStack(), this::add, context, flag, false) }
        }
    }

    private fun renderFluid(bounds: HTBounds, graphics: GuiGraphicsExtractor) {
        if (widget.isEmpty) return
        val (sprite: TextureAtlasSprite, color: Int) = HTGuiRenderHelper.getSpriteAndColor(widget.getFluidStack())
            ?: return

        val spriteContents: SpriteContents = sprite.contents()
        val tileScaling = GuiSpriteScaling.Tile(spriteContents.width(), spriteContents.height())

        var (x: Int, y: Int, width: Int, height: Int) = bounds
        x++
        y++
        width -= 2
        height -= 2
        val fixedY: Int = (height * getFilledLevel()).roundToInt()
        graphics.enableScissor(x, y, x + width, y + height)
        graphics.blitTiledSprite(
            RenderPipelines.GUI_TEXTURED,
            sprite,
            x,
            y + height - fixedY,
            width,
            fixedY,
            0,
            0,
            tileScaling.width,
            tileScaling.height,
            tileScaling.width,
            tileScaling.height,
            color
        )
        graphics.disableScissor()
    }

    private fun getFilledLevel(): Float = when (widget) {
        is HTFluidWidget.Slot -> 1f
        is HTFluidWidget.Tank -> widget.currentFilledLevel
    }.coerceAtMost(1f)
}
