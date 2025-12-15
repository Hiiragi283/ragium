package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTBoundsRenderer
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.client.gui.component.base.HTBasicFluidWidget
import net.minecraft.client.gui.GuiGraphics
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTFluidSlotWidget(private val view: HTFluidView, x: Int, y: Int) :
    HTBasicFluidWidget(x, y, 16, 16),
    HTFluidView by view {
    companion object {
        @JvmStatic
        private val BACKGROUND: HTBoundsRenderer = HTBoundsRenderer.fromSprite(RagiumAPI.id("textures", "gui", "fluid_slot.png"))
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        BACKGROUND.render(guiGraphics, getBounds())
    }
}
