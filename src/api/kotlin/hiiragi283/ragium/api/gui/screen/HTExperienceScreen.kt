package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.gui.component.HTExperienceWidget
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTExperienceScreen : HTPositionScreen {
    fun getExperienceWidget(): HTExperienceWidget?
}
