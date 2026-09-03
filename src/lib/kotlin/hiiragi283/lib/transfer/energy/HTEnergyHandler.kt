package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.util.fixedFraction
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTEnergyHandler {
    val amount: Int
    val capacity: Int

    val isEmpty: Boolean get() = amount == 0

    val needed: Int get() = capacity - amount
    val filledLevel: Float get() = fixedFraction(amount, capacity)

    fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    @Suppress("NonExtendableApiUsage")
    fun asForge(): EnergyHandler = object : EnergyHandler {
        override fun getAmountAsLong(): Long = amountAsInt.toLong()

        override fun getAmountAsInt(): Int = this@HTEnergyHandler.amount

        override fun getCapacityAsLong(): Long = capacityAsInt.toLong()

        override fun getCapacityAsInt(): Int = this@HTEnergyHandler.capacity

        override fun insert(amount: Int, transaction: TransactionContext): Int =
            this@HTEnergyHandler.insert(amount, transaction, HTTransferAccess.EXTERNAL)

        override fun extract(amount: Int, transaction: TransactionContext): Int =
            this@HTEnergyHandler.extract(amount, transaction, HTTransferAccess.EXTERNAL)
    }
}
