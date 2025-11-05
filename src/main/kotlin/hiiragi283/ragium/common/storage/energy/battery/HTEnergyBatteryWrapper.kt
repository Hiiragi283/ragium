package hiiragi283.ragium.common.storage.energy.battery

import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery

class HTEnergyBatteryWrapper(private val getter: () -> HTEnergyBattery?) : HTEnergyBattery {
    private val delegate: HTEnergyBattery? get() = getter()

    override fun getAmount(): Int = delegate?.getAmount() ?: 0

    override fun getCapacity(): Int = delegate?.getCapacity() ?: 0

    override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = delegate?.insert(amount, action, access) ?: 0

    override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
        delegate?.extract(amount, action, access) ?: 0

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
