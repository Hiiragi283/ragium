package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.gui.component.HTEnergyWidget
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTEnergyScreen : HTPositionScreen {
    fun getEnergyWidget(): HTEnergyWidget?
}
