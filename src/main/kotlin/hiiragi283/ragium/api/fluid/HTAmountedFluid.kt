package hiiragi283.ragium.api.fluid

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids

data class HTAmountedFluid(val fluid: Fluid, val amount: Long) {
    companion object {
        @JvmField
        val EMPTY = HTAmountedFluid(Fluids.EMPTY, 0)
    }

    val isEmpty: Boolean = fluid == Fluids.EMPTY || amount <= 0
}
