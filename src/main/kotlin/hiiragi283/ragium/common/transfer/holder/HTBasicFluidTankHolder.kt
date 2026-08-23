package hiiragi283.ragium.common.transfer.holder

import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder

class HTBasicFluidTankHolder private constructor(
    configGetter: HTSlotInfoProvider?,
    slots: List<HTFluidTank>,
    slotMap: Map<HTSlotInfo, List<HTFluidTank>>,
) : HTSlottedCapabilityHolder<HTFluidTank>(configGetter, slots, slotMap),
    HTResourceSlotHolder<HTFluidTank> {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTSlotInfoProvider?): Builder = Builder(configGetter)
    }

    class Builder(configGetter: HTSlotInfoProvider?) : HTSlottedCapabilityHolder.Builder<HTFluidTank, HTResourceSlotHolder<HTFluidTank>>(configGetter, ::HTBasicFluidTankHolder)
}
