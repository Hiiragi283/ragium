package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage

class HTEnergyStorageWrapper(private val getter: () -> HTEnergyStorage?) : HTEnergyStorage {
    private val delegate: HTEnergyStorage? get() = getter()

    override fun getAmount(): Int = delegate?.getAmount() ?: 0

    override fun getCapacity(): Int = delegate?.getCapacity() ?: 0

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
