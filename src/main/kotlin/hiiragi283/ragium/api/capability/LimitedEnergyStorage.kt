package hiiragi283.ragium.api.capability

import com.google.common.base.Suppliers
import net.neoforged.neoforge.energy.IEnergyStorage
import java.util.function.Supplier

class LimitedEnergyStorage(val storageIO: HTStorageIO, val delegate: Supplier<IEnergyStorage>) : IEnergyStorage {
    constructor(storageIO: HTStorageIO, delegate: IEnergyStorage) : this(storageIO, Suppliers.ofInstance(delegate))

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!storageIO.canInsert) return 0
        return delegate.get().receiveEnergy(toReceive, simulate)
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!storageIO.canExtract) return 0
        return delegate.get().extractEnergy(toExtract, simulate)
    }

    override fun getEnergyStored(): Int = delegate.get().energyStored

    override fun getMaxEnergyStored(): Int = delegate.get().maxEnergyStored

    override fun canExtract(): Boolean = delegate.get().canExtract() && storageIO.canExtract

    override fun canReceive(): Boolean = delegate.get().canReceive() && storageIO.canInsert
}
