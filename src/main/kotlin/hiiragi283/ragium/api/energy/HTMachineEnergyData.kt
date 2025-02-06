package hiiragi283.ragium.api.energy

import net.neoforged.neoforge.energy.IEnergyStorage

sealed class HTMachineEnergyData(val amount: Int, val energyHandler: IEnergyStorage.(Int, Boolean) -> Int) {
    class Consume(amount: Int) : HTMachineEnergyData(amount, IEnergyStorage::extractEnergy)

    class Generated(amount: Int) : HTMachineEnergyData(amount, IEnergyStorage::receiveEnergy)
}
