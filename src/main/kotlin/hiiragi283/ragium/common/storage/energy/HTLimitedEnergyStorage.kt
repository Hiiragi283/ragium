package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.storage.HTStorageIO
import net.neoforged.neoforge.energy.IEnergyStorage

class HTLimitedEnergyStorage(val storageIO: HTStorageIO, val delegate: IEnergyStorage) : IEnergyStorage {
    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!storageIO.canInsert) return 0
        return delegate.receiveEnergy(toReceive, simulate)
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!storageIO.canExtract) return 0
        return delegate.extractEnergy(toExtract, simulate)
    }

    override fun getEnergyStored(): Int = delegate.energyStored

    override fun getMaxEnergyStored(): Int = delegate.maxEnergyStored

    override fun canExtract(): Boolean = delegate.canExtract() && storageIO.canExtract

    override fun canReceive(): Boolean = delegate.canReceive() && storageIO.canInsert
}
