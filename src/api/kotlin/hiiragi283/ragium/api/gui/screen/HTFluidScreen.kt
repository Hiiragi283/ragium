package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTFluidScreen : HTPositionScreen {
    fun getFluidWidgets(): List<HTFluidWidget>
}
