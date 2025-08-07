package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTFluidTankWidget(
    override var stack: FluidStack,
    override val capacity: Int,
    x: Int,
    y: Int,
) : HTFluidWidget(
        x,
        y,
        Component.empty(),
    )
