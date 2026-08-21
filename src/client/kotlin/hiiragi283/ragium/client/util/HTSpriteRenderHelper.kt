package hiiragi283.ragium.client.util

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.resource.modifyPath
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

object HTSpriteRenderHelper {
    @JvmStatic
    fun fixTextureId(id: Identifier): Identifier = id.modifyPath { path: String ->
        when {
            path.endsWith(".png") -> path
            else -> "$path.png"
        }
    }

    //    Renderer    //

    @JvmStatic
    fun blit(
        graphics: GuiGraphicsExtractor,
        texture: Identifier,
        bounds: HTBounds,
        uOffset: Int = 0,
        vOffset: Int = 0,
        textureWidth: Int = bounds.width,
        textureHeight: Int = bounds.height,
    ) {
        val (x: Int, y: Int, width: Int, height: Int) = bounds
        blit(
            graphics,
            texture,
            x,
            y,
            width,
            height,
            uOffset,
            vOffset,
            textureWidth,
            textureHeight,
        )
    }

    @JvmStatic
    fun blit(
        graphics: GuiGraphicsExtractor,
        texture: Identifier,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        uOffset: Int = 0,
        vOffset: Int = 0,
        textureWidth: Int = width,
        textureHeight: Int = height,
    ) {
        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            fixTextureId(texture),
            x,
            y,
            uOffset,
            vOffset,
            width,
            height,
            textureWidth,
            textureHeight,
        )
    }
}
