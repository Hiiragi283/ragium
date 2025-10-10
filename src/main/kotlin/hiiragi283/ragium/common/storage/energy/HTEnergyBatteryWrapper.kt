package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery

class HTEnergyBatteryWrapper(private val getter: () -> HTEnergyBattery?) : HTEnergyBattery {
    private val delegate: HTEnergyBattery? get() = getter()

    override fun getAmountAsLong(): Long = delegate?.getAmountAsLong() ?: 0

    override fun getCapacityAsLong(): Long = delegate?.getCapacityAsLong() ?: 0

    override fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
        delegate?.insertEnergy(amount, action, access) ?: 0

    override fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
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
