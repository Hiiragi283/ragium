package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
interface HTFluidScreen :
    HTPositionScreen,
    Iterable<HTFluidWidget> {
    fun setFluidStack(index: Int, stack: FluidStack)
}
