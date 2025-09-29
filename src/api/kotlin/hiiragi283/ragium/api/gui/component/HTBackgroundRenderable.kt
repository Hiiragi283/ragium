package hiiragi283.ragium.api.gui.component

import net.minecraft.client.gui.GuiGraphics
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
fun interface HTBackgroundRenderable {
    companion object {
        @JvmStatic
        fun tryRender(renderable: Any?, guiGraphics: GuiGraphics) {
            if (renderable is HTBackgroundRenderable) {
                renderable.renderBackground(guiGraphics)
            }
        }
    }

    fun renderBackground(guiGraphics: GuiGraphics)
}
