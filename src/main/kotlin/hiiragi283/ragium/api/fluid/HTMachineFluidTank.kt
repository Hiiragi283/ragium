package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.capability.HTSlotHandler
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

/**
 * Ragiumで使用する[FluidTank]の拡張クラス
 * @param callback [FluidTank.onContentsChanged]で呼び出されるブロック
 */
open class HTMachineFluidTank(capacity: Int, val callback: () -> Unit) :
    FluidTank(capacity),
    HTSlotHandler<FluidStack> {
    override fun onContentsChanged() {
        callback()
    }

    //    HTSlotHandler    //

    override var stack: FluidStack
        get() = fluid
        set(value) = setFluid(value)
}
