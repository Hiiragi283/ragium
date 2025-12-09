package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.math.HTBounds
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

fun interface HTBoundsRenderer {
    companion object {
        @JvmStatic
        fun fromSprite(texture: ResourceLocation): HTBoundsRenderer = HTBoundsRenderer { graphics: GuiGraphics, bounds: HTBounds ->
            graphics.blit(
                texture,
                bounds.x - 1,
                bounds.y - 1,
                0f,
                0f,
                bounds.width + 2,
                bounds.height + 2,
                bounds.width + 2,
                bounds.height + 2,
            )
        }
    }

    fun render(guiGraphics: GuiGraphics, bounds: HTBounds)
}
