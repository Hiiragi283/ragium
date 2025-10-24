package hiiragi283.ragium.common.storage.proxy

import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTSidedEnergyStorage
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * [IEnergyStorage]向けの[HTProxyHandler]の実装クラス
 * @param storage ラップ対象の[HTSidedEnergyStorage]
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @see mekanism.common.capabilities.proxy.ProxyStrictEnergyHandler
 */
class HTProxyEnergyStorage(private val storage: HTSidedEnergyStorage, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    IEnergyStorage {
    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = when (readOnlyInsert) {
        true -> 0
        false -> storage.receiveEnergy(toReceive, HTStorageAction.of(simulate), side)
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = when (readOnlyExtract) {
        true -> 0
        false -> storage.extractEnergy(toExtract, HTStorageAction.of(simulate), side)
    }

    override fun getEnergyStored(): Int = storage.getEnergyStored(side)

    override fun getMaxEnergyStored(): Int = storage.getMaxEnergyStored(side)

    override fun canExtract(): Boolean = !readOnlyExtract && storage.canExtract(side)

    override fun canReceive(): Boolean = !readOnlyInsert && storage.canReceive(side)
}
