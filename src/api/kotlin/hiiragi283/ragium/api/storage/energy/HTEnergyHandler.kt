package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.storage.HTContentListener
import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * [IEnergyStorage]に基づいた[HTSidedEnergyStorage]の拡張インターフェース
 * @see [mekanism.api.energy.IMekanismStrictEnergyHandler]
 */
interface HTEnergyHandler :
    HTSidedEnergyStorage,
    HTContentListener {
    fun hasEnergyStorage(): Boolean = true

    fun getEnergyHandler(side: Direction?): IEnergyStorage?

    override fun receiveEnergy(toReceive: Int, simulate: Boolean, side: Direction?): Int =
        getEnergyHandler(side)?.receiveEnergy(toReceive, simulate) ?: 0

    override fun extractEnergy(toExtract: Int, simulate: Boolean, side: Direction?): Int =
        getEnergyHandler(side)?.extractEnergy(toExtract, simulate) ?: 0

    override fun getEnergyStored(side: Direction?): Int = getEnergyHandler(side)?.energyStored ?: 0

    override fun getMaxEnergyStored(side: Direction?): Int = getEnergyHandler(side)?.maxEnergyStored ?: 0

    override fun canExtract(side: Direction?): Boolean = getEnergyHandler(side)?.canExtract() ?: false

    override fun canReceive(side: Direction?): Boolean = getEnergyHandler(side)?.canReceive() ?: false
}
