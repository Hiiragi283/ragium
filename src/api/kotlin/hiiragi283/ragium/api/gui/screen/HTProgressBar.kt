package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.vanillaId
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
data class HTProgressBar(
    val texture: ResourceLocation,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
) {
    companion object {
        @JvmStatic
        fun arrow(x: Int, y: Int): HTProgressBar = HTProgressBar(vanillaId("container/furnace/burn_progress"), x, y, 24, 16)

        @JvmStatic
        fun burn(x: Int, y: Int): HTProgressBar = HTProgressBar(RagiumAPI.id("container/burn_progress"), x, y, 16, 16)

        @JvmStatic
        fun infuse(x: Int, y: Int): HTProgressBar = HTProgressBar(RagiumAPI.id("container/infuse_progress"), x, y, 24, 16)
    }

    fun render(guiGraphics: GuiGraphics, screen: HTContainerScreen<*>, level: Float) {
        guiGraphics.blitSprite(
            texture,
            width,
            height,
            0,
            0,
            screen.startX + x,
            screen.startY + y,
            Mth.ceil(level * width),
            height,
        )
    }
}
