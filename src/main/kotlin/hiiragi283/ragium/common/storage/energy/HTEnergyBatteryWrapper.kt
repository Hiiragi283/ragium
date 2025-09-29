package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput

class HTEnergyBatteryWrapper(private val getter: () -> HTEnergyBattery?) : HTEnergyBattery {
    val delegate: HTEnergyBattery? get() = getter()

    override fun getAmount(): Int = delegate?.getAmount() ?: 0

    override fun setAmount(amount: Int) {
        delegate?.setAmount(amount)
    }

    override fun getCapacity(): Int = delegate?.getCapacity() ?: 0

    override fun insertEnergy(amount: Int, simulate: Boolean, access: HTStorageAccess): Int =
        delegate?.insertEnergy(amount, simulate, access) ?: 0

    override fun extractEnergy(amount: Int, simulate: Boolean, access: HTStorageAccess): Int =
        delegate?.extractEnergy(amount, simulate, access) ?: 0

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
