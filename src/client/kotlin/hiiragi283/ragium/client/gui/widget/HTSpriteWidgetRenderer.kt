package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.client.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.widget.HTWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.SpriteContents
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling
import net.minecraft.util.FormattedCharSequence
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.client.ClientTooltipFlag

abstract class HTSpriteWidgetRenderer<WIDGET : HTWidget>(gui: HTGuiAccess, widget: WIDGET) : HTAbstractWidgetRenderer<WIDGET>(gui, widget) {
    protected val font: Font = Minecraft.getInstance().font

    override fun render(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Render background
        renderBackground(bounds, graphics)
        // Render sprite
        renderSprite(bounds, graphics)
        // Render tooltip
        if (bounds.contains(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(
                font,
                collectTooltips(getTooltipFlag()),
                mouseX,
                mouseY,
            )
        }
    }

    private fun renderSprite(bounds: HTBounds, graphics: GuiGraphicsExtractor) {
        if (!shouldRender()) return
        val sprite: TextureAtlasSprite = getSprite() ?: return
        val color: Int = getColor()
        val fillLevel: Float = getScaledLevel()

        val spriteContents: SpriteContents = sprite.contents()
        val tileScaling = GuiSpriteScaling.Tile(spriteContents.width(), spriteContents.height())

        var (x: Int, y: Int, width: Int, height: Int) = bounds
        x++
        y++
        width -= 2
        height -= 2
        graphics.blitTiledSprite(
            RenderPipelines.GUI_TEXTURED,
            sprite,
            x,
            y,
            width,
            height,
            0,
            0,
            tileScaling.width,
            tileScaling.height,
            tileScaling.width,
            tileScaling.height,
            color,
        ) // TODO
    }

    protected fun getTooltipFlag(): TooltipFlag = ClientTooltipFlag.of(
        when (Minecraft.getInstance().options.advancedItemTooltips) {
            true -> TooltipFlag.ADVANCED
            false -> TooltipFlag.NORMAL
        },
    )

    protected abstract fun renderBackground(bounds: HTBounds, graphics: GuiGraphicsExtractor)

    protected abstract fun shouldRender(): Boolean

    protected abstract fun getSprite(): TextureAtlasSprite?

    protected abstract fun getColor(): Int

    protected open fun getScaledLevel(): Float = getLevel() * (widget.bounds.height - 2)

    protected abstract fun getLevel(): Float

    protected abstract fun collectTooltips(flag: TooltipFlag): List<FormattedCharSequence>
}
