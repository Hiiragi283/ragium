package hiiragi283.ragium.client.gui.component

import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.client.gui.component.HTBasicFluidWidget
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class HTFluidTankWidget(protected open val view: HTFluidView, x: Int, y: Int) :
    HTBasicFluidWidget(
        x,
        y,
        16,
        18 * 3 - 2,
    ),
    HTFluidView by view
