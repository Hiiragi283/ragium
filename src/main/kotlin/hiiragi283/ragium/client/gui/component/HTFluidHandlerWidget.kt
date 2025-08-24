package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

@OnlyIn(Dist.CLIENT)
class HTFluidHandlerWidget(
    private val handler: IFluidHandler?,
    private val index: Int,
    x: Int,
    y: Int,
) : HTFluidWidget(x, y, Component.empty()) {
    override var stack: FluidStack
        get() = handler?.getFluidInTank(index) ?: FluidStack.EMPTY
        set(value) {}
    override val capacity: Int
        get() = handler?.getTankCapacity(index) ?: 0
}
