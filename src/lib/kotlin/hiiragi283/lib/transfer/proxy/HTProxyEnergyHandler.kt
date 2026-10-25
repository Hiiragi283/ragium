package hiiragi283.lib.transfer.proxy

import hiiragi283.lib.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [EnergyHandler]向けの[HTProxyHandler]の拡張クラスです。
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
class HTProxyEnergyHandler(private val handler: EnergyHandler, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    EnergyHandler {
    override fun getAmountAsLong(): Long = handler.amountAsLong

    override fun getCapacityAsLong(): Long = handler.capacityAsLong

    override fun insert(amount: Int, transaction: TransactionContext): Int = when {
        readOnlyInsert -> 0
        else -> handler.insert(amount, transaction)
    }

    override fun extract(amount: Int, transaction: TransactionContext): Int = when {
        readOnlyExtract -> 0
        else -> handler.extract(amount, transaction)
    }
}
