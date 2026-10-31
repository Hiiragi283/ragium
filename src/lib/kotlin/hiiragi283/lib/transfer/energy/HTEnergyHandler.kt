package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTTransferAccess
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTEnergyHandler(val storage: HTEnergyStorage) : EnergyHandler {
    override fun getAmountAsLong(): Long = storage.amount.toLong()

    override fun getCapacityAsLong(): Long = storage.capacity.toLong()

    override fun insert(amount: Int, transaction: TransactionContext): Int = storage.insert(amount, transaction, HTTransferAccess.EXTERNAL)

    override fun extract(amount: Int, transaction: TransactionContext): Int = storage.extract(amount, transaction, HTTransferAccess.EXTERNAL)
}
