package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.client.gui.component.base.HTAbstractWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction

@OnlyIn(Dist.CLIENT)
class HTProgressWidget(
    private val texture: ResourceLocation,
    private val levelGetter: () -> Fraction,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTAbstractWidget(x, y, width, height, Component.empty()) {
    companion object {
        @JvmStatic
        fun arrow(levelGetter: () -> Fraction, x: Int, y: Int): HTProgressWidget =
            HTProgressWidget(vanillaId("container", "furnace/burn_progress"), levelGetter, x, y, 24, 16)

        @JvmStatic
        fun burn(levelGetter: () -> Fraction, x: Int, y: Int): HTProgressWidget =
            HTProgressWidget(RagiumAPI.id("container", "burn_progress"), levelGetter, x, y, 16, 16)

        @JvmStatic
        fun infuse(levelGetter: () -> Fraction, x: Int, y: Int): HTProgressWidget =
            HTProgressWidget(RagiumAPI.id("container", "infuse_progress"), levelGetter, x, y, 24, 16)
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
            (levelGetter() * width).toInt(),
            height,
        )
    }
}
