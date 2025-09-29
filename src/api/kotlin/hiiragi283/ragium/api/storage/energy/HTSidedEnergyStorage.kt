package hiiragi283.ragium.api.storage.energy

import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * 向きに応じて制御された[IEnergyStorage]の拡張インターフェース
 * @see [mekanism.api.energy.ISidedStrictEnergyHandler]
 */
interface HTSidedEnergyStorage : IEnergyStorage {
    fun getEnergySideFor(): Direction? = null

    fun receiveEnergy(toReceive: Int, simulate: Boolean, side: Direction?): Int

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = receiveEnergy(toReceive, simulate, getEnergySideFor())

    fun extractEnergy(toExtract: Int, simulate: Boolean, side: Direction?): Int

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = extractEnergy(toExtract, simulate, getEnergySideFor())

    fun getEnergyStored(side: Direction?): Int

    override fun getEnergyStored(): Int = getEnergyStored(getEnergySideFor())

    fun getMaxEnergyStored(side: Direction?): Int

    override fun getMaxEnergyStored(): Int = getMaxEnergyStored(getEnergySideFor())

    fun canExtract(side: Direction?): Boolean

    override fun canExtract(): Boolean = canExtract(getEnergySideFor())

    fun canReceive(side: Direction?): Boolean

    override fun canReceive(): Boolean = canReceive(getEnergySideFor())
}
