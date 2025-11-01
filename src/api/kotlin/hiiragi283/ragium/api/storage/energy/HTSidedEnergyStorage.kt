package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * 向きに応じて制御された[IEnergyStorage]の拡張インターフェース
 * @see mekanism.api.energy.ISidedStrictEnergyHandler
 */
interface HTSidedEnergyStorage : IEnergyStorage {
    fun getEnergySideFor(): Direction? = null

    fun receiveEnergy(toReceive: Int, action: HTStorageAction, side: Direction?): Int

    @Deprecated("Use `receiveEnergy(Int, Boolean, Direction?) instead`")
    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int =
        receiveEnergy(toReceive, HTStorageAction.of(simulate), getEnergySideFor())

    fun extractEnergy(toExtract: Int, action: HTStorageAction, side: Direction?): Int

    @Deprecated("Use `extractEnergy(Int, Boolean, Direction?) instead`")
    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int =
        extractEnergy(toExtract, HTStorageAction.of(simulate), getEnergySideFor())

    fun getEnergyStored(side: Direction?): Int

    @Deprecated("Use `getEnergyStored(Direction?) instead`")
    override fun getEnergyStored(): Int = getEnergyStored(getEnergySideFor())

    fun getMaxEnergyStored(side: Direction?): Int

    @Deprecated("Use `getMaxEnergyStored(Direction?) instead`")
    override fun getMaxEnergyStored(): Int = getMaxEnergyStored(getEnergySideFor())

    override fun canExtract(): Boolean = true

    override fun canReceive(): Boolean = true
}
