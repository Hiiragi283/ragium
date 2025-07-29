package hiiragi283.ragium.api.storage.energy

import net.neoforged.neoforge.energy.IEnergyStorage

class HTFilteredEnergyStorage(val delegate: IEnergyStorage, val filter: HTEnergyFilter) : IEnergyStorage {
    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!filter.canReceive) return 0
        return delegate.receiveEnergy(toReceive, simulate)
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!filter.canExtract) return 0
        return delegate.extractEnergy(toExtract, simulate)
    }

    override fun getEnergyStored(): Int = delegate.energyStored

    override fun getMaxEnergyStored(): Int = delegate.maxEnergyStored

    override fun canExtract(): Boolean = delegate.canExtract() && filter.canExtract

    override fun canReceive(): Boolean = delegate.canReceive() && filter.canReceive
}
