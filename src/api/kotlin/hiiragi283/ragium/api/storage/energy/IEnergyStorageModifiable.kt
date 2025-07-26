package hiiragi283.ragium.api.storage.energy

import net.neoforged.neoforge.energy.IEnergyStorage

interface IEnergyStorageModifiable : IEnergyStorage {
    fun setEnergyStored(amount: Int)

    fun setMaxEnergyStored(capacity: Int)
}
