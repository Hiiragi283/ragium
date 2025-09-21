package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.client.gui.GuiGraphics
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTFluidSlotWidget(tank: HTFluidTank, x: Int, y: Int) : HTFluidWidgetBase(tank, x, y, 16, 16) {
    override fun getLevel(): Float = 1f

    override fun renderBackground(guiGraphics: GuiGraphics) {}
}
