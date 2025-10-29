package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.storage.HTAmountView
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTExperienceWidget :
    HTAmountView.LongSized,
    HTWidget {
    fun setAmount(amount: Long)
}
