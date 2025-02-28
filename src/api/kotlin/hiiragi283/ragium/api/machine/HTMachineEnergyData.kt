package hiiragi283.ragium.api.machine

import net.neoforged.neoforge.energy.IEnergyStorage

interface HTMachineEnergyData {
    val amount: Int

    fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Result<Unit>

    data class Empty(private val result: Boolean) : HTMachineEnergyData {
        override val amount: Int = 0

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Result<Unit> = Result.success(Unit)
    }

    enum class Consume(override val amount: Int) : HTMachineEnergyData {
        DEFAULT(160),
        CHEMICAL(640),
        PRECISION(2560),
        ;

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Result<Unit> = runCatching {
            val fixedAmount: Int = amount * modifier
            if (fixedAmount <= 0 || storage.extractEnergy(fixedAmount, simulate) != fixedAmount) {
                throw HTMachineException.ConsumeEnergy()
            }
        }
    }

    enum class Generate(override val amount: Int) : HTMachineEnergyData {
        DEFAULT(320),
        CHEMICAL(1280),
        PRECISION(5120),
        ;

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Result<Unit> = runCatching {
            val fixedAmount: Int = amount * modifier
            if (fixedAmount <= 0 || storage.receiveEnergy(fixedAmount, simulate) == 0) {
                throw HTMachineException.GenerateEnergy()
            }
        }
    }
}
