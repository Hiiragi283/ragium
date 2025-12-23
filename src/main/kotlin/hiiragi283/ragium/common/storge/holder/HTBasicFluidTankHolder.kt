package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.collection.ImmutableMultiMap
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import net.minecraft.core.Direction

class HTBasicFluidTankHolder private constructor(
    configGetter: HTSlotInfoProvider?,
    slots: List<HTFluidTank>,
    slotMap: ImmutableMultiMap<HTSlotInfo, HTFluidTank>,
) : HTSlottedCapabilityHolder<HTFluidTank>(configGetter, slots, slotMap),
    HTFluidTankHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTSlotInfoProvider?): Builder = Builder(configGetter)
    }

    override fun getFluidTank(side: Direction?): List<HTFluidTank> = getSlots(side)

    class Builder(configGetter: HTSlotInfoProvider?) :
        HTSlottedCapabilityHolder.Builder<HTFluidTank, HTFluidTankHolder>(configGetter, ::HTBasicFluidTankHolder)
}
