package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.storage.fluid.ImmutableFluidStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTFluidScreen : HTPositionScreen {
    fun setFluidStack(index: Int, stack: ImmutableFluidStack)

    fun getFluidWidgets(): Iterable<HTFluidWidget>
}
