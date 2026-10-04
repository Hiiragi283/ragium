package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTGuiRenderHelper
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.text.Text
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.SpriteContents
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import java.util.function.Consumer

abstract class HTSpriteWidgetRenderer<WIDGET : HTWidget>(gui: HTGuiAccess, widget: WIDGET) :
    HTAbstractWidgetRenderer<WIDGET>(gui, widget) {
    protected val font: Font = Minecraft.getInstance().font

    override fun render(
        bounds: HTBounds,
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        // Render background
        renderBackground(bounds, graphics)
        // Render sprite
        renderSprite(bounds, graphics)
        // Render tooltip
        if (bounds.contains(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(
                font,
                buildList {
                    collectTooltips(
                        { text: Text -> this.add(text.visualOrderText) },
                        HTGuiRenderHelper.getTooltipContext(),
                        HTGuiRenderHelper.getTooltipFlag()
                    )
                },
                mouseX,
                mouseY
            )
        }
    }

    private fun renderSprite(bounds: HTBounds, graphics: GuiGraphicsExtractor) {
        if (!shouldRender()) return
        val (sprite: TextureAtlasSprite, color: Int) = getSpriteAndColor() ?: return
        val fillLevel: Int = getScaledLevel().toInt()

        val spriteContents: SpriteContents = sprite.contents()
        val tileScaling = GuiSpriteScaling.Tile(spriteContents.width(), spriteContents.height())

        var (x: Int, y: Int, width: Int, height: Int) = bounds
        x++
        y++
        width -= 2
        height -= 2
        graphics.enableScissor(x, y, x + width, y + height)
        graphics.blitTiledSprite(
            RenderPipelines.GUI_TEXTURED,
            sprite,
            x,
            y + height - fillLevel,
            width,
            fillLevel,
            0,
            0,
            tileScaling.width,
            tileScaling.height,
            tileScaling.width,
            tileScaling.height,
            color
        ) // TODO
        graphics.disableScissor()
    }

    protected abstract fun renderBackground(bounds: HTBounds, graphics: GuiGraphicsExtractor)

    protected abstract fun shouldRender(): Boolean

    protected abstract fun getSpriteAndColor(): Pair<TextureAtlasSprite, Int>?

    protected fun getSprite(atlasId: Identifier, id: Identifier): TextureAtlasSprite = Minecraft
        .getInstance()
        .atlasManager
        .getAtlasOrThrow(atlasId)
        .getSprite(id)

    protected open fun getScaledLevel(): Float = getLevel() * (widget.bounds.height - 2)

    protected abstract fun getLevel(): Float

    protected abstract fun collectTooltips(consumer: Consumer<Text>, context: Item.TooltipContext, flag: TooltipFlag)
}
