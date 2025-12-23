package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.api.access.HTAccessConfigGetter
import net.minecraft.core.Direction

class HTBasicEnergyBatteryHolder private constructor(
    configGetter: HTAccessConfigGetter?,
    slots: List<HTEnergyBattery>,
    inputSlots: List<HTEnergyBattery>,
    outputSlots: List<HTEnergyBattery>,
) : HTSlottedCapabilityHolder<HTEnergyBattery>(configGetter, slots, inputSlots, outputSlots),
    HTEnergyBatteryHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTAccessConfigGetter?): Builder = Builder(configGetter)
    }

    override fun getEnergyBatteries(side: Direction?): List<HTEnergyBattery> = getSlots(side)

    class Builder(configGetter: HTAccessConfigGetter?) :
        HTSlottedCapabilityHolder.Builder<HTEnergyBattery, HTEnergyBatteryHolder>(configGetter, ::HTBasicEnergyBatteryHolder)
}
