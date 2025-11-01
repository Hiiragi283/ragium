package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import net.minecraft.core.Direction

class HTBasicEnergyBatteryHolder private constructor(
    configGetter: HTAccessConfigGetter?,
    slots: List<HTEnergyBattery>,
    map: ImmutableMultiMap<HTAccessConfig, HTEnergyBattery>,
) : HTSlottedCapabilityHolder<HTEnergyBattery>(configGetter, slots, map),
    HTEnergyBatteryHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTAccessConfigGetter?): Builder = Builder(configGetter)
    }

    override fun getEnergyBatteries(side: Direction?): List<HTEnergyBattery> = getSlots(side)

    class Builder(configGetter: HTAccessConfigGetter?) :
        HTSlottedCapabilityHolder.Builder<HTEnergyBattery, HTEnergyBatteryHolder>(configGetter, ::HTBasicEnergyBatteryHolder)
}
