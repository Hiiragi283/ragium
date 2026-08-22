package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTTransferAccess
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTEnergyHandler {
    val amount: Int
    val capacity: Int

    val needed: Int get() = capacity - amount

    fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    @Suppress("NonExtendableApiUsage")
    class Wrapper(private val delegate: HTEnergyHandler) : EnergyHandler {
        override fun getAmountAsLong(): Long = amountAsInt.toLong()

        override fun getAmountAsInt(): Int = delegate.amount

        override fun getCapacityAsLong(): Long = capacityAsInt.toLong()

        override fun getCapacityAsInt(): Int = delegate.capacity

        override fun insert(amount: Int, transaction: TransactionContext): Int = delegate.insert(amount, transaction, HTTransferAccess.EXTERNAL)

        override fun extract(amount: Int, transaction: TransactionContext): Int = delegate.extract(amount, transaction, HTTransferAccess.EXTERNAL)

        override fun toString(): String = "HTEnergyHandler.Wrapper(delegate=$delegate)"
    }
}
