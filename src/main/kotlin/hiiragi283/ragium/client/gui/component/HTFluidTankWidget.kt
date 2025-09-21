package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.client.gui.GuiGraphics
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTFluidTankWidget(tank: HTFluidTank, x: Int, y: Int) :
    HTFluidWidgetBase(
        tank,
        x,
        y,
        16,
        18 * 3 - 2,
    ) {
    override fun getLevel(): Float {
        if (capacity <= 0) return 0f
        return stack.amount / capacity.toFloat()
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        guiGraphics.blit(
            HTFluidWidget.TEXTURE_ID,
            x - 1,
            y - 1,
            0f,
            0f,
            width + 2,
            height + 2,
            width + 2,
            height + 2,
        )
    }
}
