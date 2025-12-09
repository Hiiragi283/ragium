package hiiragi283.ragium.api.gui.component

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
fun interface HTBackgroundRenderable {
    companion object {
        @JvmStatic
        fun tryRender(renderable: Any?, guiGraphics: GuiGraphics) {
            if (renderable is HTBackgroundRenderable) {
                if (renderable is AbstractWidget && !renderable.visible) return
                renderable.renderBackground(guiGraphics)
            }
        }
    }

    fun renderBackground(guiGraphics: GuiGraphics)
}
