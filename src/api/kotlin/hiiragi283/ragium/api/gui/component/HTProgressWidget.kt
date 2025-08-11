package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.vanillaId
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth

class HTProgressWidget(
    private val texture: ResourceLocation,
    private val levelGetter: () -> Float,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : AbstractWidget(x, y, width, height, Component.empty()) {
    companion object {
        @JvmStatic
        fun arrow(levelGetter: () -> Float, x: Int, y: Int): HTProgressWidget =
            HTProgressWidget(vanillaId("container/furnace/burn_progress"), levelGetter, x, y, 24, 16)

        @JvmStatic
        fun burn(levelGetter: () -> Float, x: Int, y: Int): HTProgressWidget =
            HTProgressWidget(RagiumAPI.id("container/burn_progress"), levelGetter, x, y, 16, 16)

        @JvmStatic
        fun infuse(levelGetter: () -> Float, x: Int, y: Int): HTProgressWidget =
            HTProgressWidget(RagiumAPI.id("container/infuse_progress"), levelGetter, x, y, 24, 16)
    }

    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        guiGraphics.blitSprite(
            texture,
            width,
            height,
            0,
            0,
            x,
            y,
            Mth.ceil(levelGetter() * width),
            height,
        )
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {}
}
