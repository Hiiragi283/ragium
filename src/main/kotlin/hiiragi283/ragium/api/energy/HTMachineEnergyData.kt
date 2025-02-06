package hiiragi283.ragium.api.energy

import net.neoforged.neoforge.energy.IEnergyStorage

sealed class HTMachineEnergyData(val amount: Int, val energyHandler: IEnergyStorage.(Int, Boolean) -> Int) {
    companion object {
        @JvmField
        val EMPTY: HTMachineEnergyData = Consume(0)

        @JvmStatic
        fun consume(amount: Int): HTMachineEnergyData = Consume(amount)

        @JvmStatic
        fun generate(amount: Int): HTMachineEnergyData = Generate(amount)
    }

    private class Consume(amount: Int) : HTMachineEnergyData(amount, IEnergyStorage::extractEnergy)

    private class Generate(amount: Int) : HTMachineEnergyData(amount, IEnergyStorage::receiveEnergy)
}
