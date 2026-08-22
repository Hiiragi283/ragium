package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTResourceView
import hiiragi283.lib.transfer.HTTransferAccess
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTInputSlot<T : Resource> {
    fun extract(amount: Int, transaction: TransactionContext): Int

    fun canExtract(amount: Int, transaction: TransactionContext): Boolean = extract(amount, transaction) == amount

    //    Single    //

    data class Single<T : Resource>(private val slot: HTResourceSlot<T>) :
        HTInputSlot<T>,
        HTResourceView<T> by slot {
        override fun extract(amount: Int, transaction: TransactionContext): Int = slot.extractSelf(amount, transaction, HTTransferAccess.INTERNAL)
    }

    //    Multiple    //

    @JvmRecord
    data class Multiple<T : Resource>(private val slots: Iterable<HTResourceSlot<T>>) : HTInputSlot<T> {
        constructor(vararg slots: HTResourceSlot<T>) : this(slots.asIterable())

        override fun extract(amount: Int, transaction: TransactionContext): Int {
            TransferPreconditions.checkNonNegative(amount)
            var extracted = 0
            for (slot: HTResourceSlot<T> in slots) {
                extracted += slot.extractSelf(amount, transaction, HTTransferAccess.INTERNAL)
                if (extracted == amount) break
            }
            return extracted
        }
    }
}
