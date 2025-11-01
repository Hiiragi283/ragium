package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.storage.HTAmountView
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTEnergyWidget :
    HTAmountView.IntSized,
    HTWidget {
    fun setAmount(amount: Int)
}
