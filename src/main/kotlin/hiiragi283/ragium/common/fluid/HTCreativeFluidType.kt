package hiiragi283.ragium.common.fluid

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.fluid.HTFluidType
import net.minecraft.network.chat.TextColor
import net.neoforged.neoforge.fluids.FluidStack

class HTCreativeFluidType(properties: Properties) : HTFluidType(properties) {
    override fun getNameColor(stack: FluidStack): TextColor = HTDefaultColor.RED.textColor
}
