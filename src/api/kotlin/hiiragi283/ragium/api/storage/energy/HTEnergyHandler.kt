package hiiragi283.ragium.api.storage.energy

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * [IEnergyStorage]に基づいた[HTSidedEnergyStorage]の拡張インターフェース
 * @see mekanism.api.energy.IMekanismStrictEnergyHandler
 */
fun interface HTEnergyHandler : HTSidedEnergyStorage {
    fun hasEnergyStorage(): Boolean = true

    fun getEnergyBattery(side: Direction?): HTEnergyBattery?

    override fun receiveEnergy(toReceive: Int, action: HTStorageAction, side: Direction?): Int =
        getEnergyBattery(side)?.insert(toReceive, action, HTStorageAccess.EXTERNAL) ?: 0

    override fun extractEnergy(toExtract: Int, action: HTStorageAction, side: Direction?): Int =
        getEnergyBattery(side)?.extract(toExtract, action, HTStorageAccess.EXTERNAL) ?: 0

    override fun getEnergyStored(side: Direction?): Int = getEnergyBattery(side)?.getAmount() ?: 0

    override fun getMaxEnergyStored(side: Direction?): Int = getEnergyBattery(side)?.getCapacity() ?: 0
}
