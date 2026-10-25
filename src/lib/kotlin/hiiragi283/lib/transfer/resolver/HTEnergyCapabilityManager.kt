package hiiragi283.lib.transfer.resolver

import hiiragi283.lib.transfer.energy.HTEnergyHandler
import hiiragi283.lib.transfer.holder.HTEnergyHandlerHolder
import hiiragi283.lib.transfer.proxy.HTProxyEnergyHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.energy.EmptyEnergyHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandler

/**
 * [EnergyHandler]向けの[HTCapabilityManager]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 **/
class HTEnergyCapabilityManager(holder: HTEnergyHandlerHolder) :
    HTCapabilityManager<HTEnergyHandlerHolder, EnergyHandler>(holder, { side: Direction?, holder: HTEnergyHandlerHolder ->
        val handler: EnergyHandler = holder.getHandler(side) ?: EmptyEnergyHandler.INSTANCE
        HTProxyEnergyHandler(handler, side, holder)
    }) {
    override fun isEmpty(side: Direction?): Boolean = getHandler(side) == null

    fun getHandler(side: Direction?): HTEnergyHandler? = holder.getHandler(side)
}
