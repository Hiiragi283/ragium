package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import net.minecraft.core.Direction

class HTBasicFluidTankHolder private constructor(
    configGetter: HTAccessConfigGetter?,
    slots: List<HTFluidTank>,
    map: ImmutableMultiMap<HTAccessConfig, HTFluidTank>,
) : HTSlottedCapabilityHolder<HTFluidTank>(configGetter, slots, map),
    HTFluidTankHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTAccessConfigGetter?): Builder = Builder(configGetter)
    }

    override fun getFluidTank(side: Direction?): List<HTFluidTank> = getSlots(side)

    class Builder(configGetter: HTAccessConfigGetter?) :
        HTSlottedCapabilityHolder.Builder<HTFluidTank, HTFluidTankHolder>(configGetter, ::HTBasicFluidTankHolder)
}
