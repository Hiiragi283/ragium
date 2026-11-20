package hiiragi283.lib.transfer.resolver

import hiiragi283.lib.transfer.energy.HTEnergyHandler
import hiiragi283.lib.transfer.energy.HTEnergyStorage
import hiiragi283.lib.transfer.holder.HTEnergyStorageHolder
import hiiragi283.lib.transfer.proxy.HTProxyEnergyHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.energy.EmptyEnergyHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandler

/**
 * [EnergyHandler]向けの[HTCapabilityManager]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 **/
class HTEnergyCapabilityManager(holder: HTEnergyStorageHolder) :
    HTCapabilityManager<HTEnergyStorageHolder, EnergyHandler>(holder, { side: Direction?, holder: HTEnergyStorageHolder ->
        val handler: EnergyHandler = holder.getStorage(side)?.let(::HTEnergyHandler) ?: EmptyEnergyHandler.INSTANCE
        HTProxyEnergyHandler(handler, side, holder)
    }) {
    override fun isEmpty(side: Direction?): Boolean = getStorage(side) == null

    fun getStorage(side: Direction?): HTEnergyStorage? = holder.getStorage(side)
}
