package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
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

    fun getEnergyBattery(side: Direction?): HTEnergyBattery?

    override fun receiveEnergy(toReceive: Int, action: HTStorageAction, side: Direction?): Int =
        getEnergyBattery(side)?.insertEnergy(toReceive, action, HTStorageAccess.EXTERNAL) ?: 0

    override fun extractEnergy(toExtract: Int, action: HTStorageAction, side: Direction?): Int =
        getEnergyBattery(side)?.extractEnergy(toExtract, action, HTStorageAccess.EXTERNAL) ?: 0

    override fun getEnergyStored(side: Direction?): Int = getEnergyBattery(side)?.getAmountAsInt() ?: 0

    override fun getMaxEnergyStored(side: Direction?): Int = getEnergyBattery(side)?.getCapacityAsInt() ?: 0

    override fun canExtract(side: Direction?): Boolean = getEnergyBattery(side) != null

    override fun canReceive(side: Direction?): Boolean = getEnergyBattery(side) != null
}
