package hiiragi283.ragium.api.energy

import net.neoforged.neoforge.energy.IEnergyStorage

sealed interface HTMachineEnergyData {
    val amount: Int

    fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean
    
    data object Empty : HTMachineEnergyData {
        override val amount: Int = 0

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean = false
    }

    enum class Consume(override val amount: Int) : HTMachineEnergyData {
        DEFAULT(160),
        CHEMICAL(640),
        PRECISION(2560),
        ;

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean =
            storage.extractEnergy(amount * modifier, simulate) == (amount * modifier)
    }

    enum class Generate(override val amount: Int) : HTMachineEnergyData {
        DEFAULT(320),
        CHEMICAL(1280),
        PRECISION(5120),
        ;

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean =
            storage.receiveEnergy(amount * modifier, simulate) >= 0
    }
}
