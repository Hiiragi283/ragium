package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.storage.fluid.HTFluidView
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTFluidWidget :
    HTFluidView,
    HTWidget
