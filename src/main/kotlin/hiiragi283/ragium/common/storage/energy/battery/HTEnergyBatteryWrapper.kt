package hiiragi283.ragium.common.storage.energy.battery

import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery

class HTEnergyBatteryWrapper(private val getter: () -> HTEnergyBattery?) : HTEnergyBattery {
    private val delegate: HTEnergyBattery? get() = getter()

    override fun getAmountAsLong(): Long = delegate?.getAmountAsLong() ?: 0

    override fun getCapacityAsLong(): Long = delegate?.getCapacityAsLong() ?: 0

    override fun insertEnergy(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long =
        delegate?.insertEnergy(amount, action, access) ?: 0

    override fun extractEnergy(amount: Long, action: HTStorageAction, access: HTStorageAccess): Long =
        delegate?.extractEnergy(amount, action, access) ?: 0

    override fun serialize(output: HTValueOutput) {
        delegate?.serialize(output)
    }

    override fun deserialize(input: HTValueInput) {
        delegate?.deserialize(input)
    }

    override fun onContentsChanged() {
        delegate?.onContentsChanged()
    }
}
