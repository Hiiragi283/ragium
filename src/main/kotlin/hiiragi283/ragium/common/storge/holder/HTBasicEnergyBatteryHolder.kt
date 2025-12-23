package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.collection.ImmutableMultiMap
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import net.minecraft.core.Direction

class HTBasicEnergyBatteryHolder private constructor(
    configGetter: HTSlotInfoProvider?,
    slots: List<HTEnergyBattery>,
    slotMap: ImmutableMultiMap<HTSlotInfo, HTEnergyBattery>,
) : HTSlottedCapabilityHolder<HTEnergyBattery>(configGetter, slots, slotMap),
    HTEnergyBatteryHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTSlotInfoProvider?): Builder = Builder(configGetter)
    }

    override fun getEnergyBatteries(side: Direction?): List<HTEnergyBattery> = getSlots(side)

    class Builder(configGetter: HTSlotInfoProvider?) :
        HTSlottedCapabilityHolder.Builder<HTEnergyBattery, HTEnergyBatteryHolder>(configGetter, ::HTBasicEnergyBatteryHolder)
}
