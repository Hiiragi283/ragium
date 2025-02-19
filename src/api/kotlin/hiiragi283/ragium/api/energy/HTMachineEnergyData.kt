package hiiragi283.ragium.api.energy

import net.neoforged.neoforge.energy.IEnergyStorage

interface HTMachineEnergyData {
    val amount: Int

    fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean

    data class Empty(private val result: Boolean) : HTMachineEnergyData {
        override val amount: Int = 0

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean = result
    }

    enum class Consume(override val amount: Int) : HTMachineEnergyData {
        DEFAULT(160),
        CHEMICAL(640),
        PRECISION(2560),
        ;

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean {
            val fixedAmount: Int = amount * modifier
            if (fixedAmount <= 0) return false
            return storage.extractEnergy(fixedAmount, simulate) == fixedAmount
        }
    }

    enum class Generate(override val amount: Int) : HTMachineEnergyData {
        DEFAULT(320),
        CHEMICAL(1280),
        PRECISION(5120),
        ;

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Boolean {
            val fixedAmount: Int = amount * modifier
            if (fixedAmount <= 0) return false
            return storage.receiveEnergy(fixedAmount, simulate) > 0
        }
    }
}
