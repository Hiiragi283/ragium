package hiiragi283.lib.transfer

import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTResourceSlot<T : Resource> :
    HTResourceView<T>,
    ValueIOSerializable {
    fun isValid(resource: T): Boolean

    fun insert(resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    fun extract(resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    fun extractSelf(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        val resourceIn: T = this.resource
        return when {
            resourceIn.isEmpty -> 0
            else -> extract(resourceIn, amount, transaction, access)
        }
    }
}
