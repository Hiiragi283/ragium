package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * Ragiumで使用される[ResourceHandler]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
interface ExtendedResourceHandler<T : Resource> : ResourceHandler<T> {
    fun insert(index: Int, resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    override fun insert(index: Int, resource: T, amount: Int, transaction: TransactionContext): Int =
        insert(index, resource, amount, transaction, HTTransferAccess.EXTERNAL)

    fun extract(index: Int, resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    override fun extract(index: Int, resource: T, amount: Int, transaction: TransactionContext): Int =
        extract(index, resource, amount, transaction, HTTransferAccess.EXTERNAL)
}
