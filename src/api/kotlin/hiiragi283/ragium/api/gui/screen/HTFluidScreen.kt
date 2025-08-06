package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import net.neoforged.neoforge.fluids.FluidStack

interface HTFluidScreen : Iterable<HTFluidWidget> {
    fun setFluidStack(index: Int, stack: FluidStack)
}
