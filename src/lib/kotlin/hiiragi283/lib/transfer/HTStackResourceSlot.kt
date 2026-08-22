package hiiragi283.lib.transfer

import java.util.function.BiPredicate
import java.util.function.Predicate
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class HTStackResourceSlot<S : Any, T : Resource> :
    SnapshotJournal<S>(),
    HTResourceSlot<T> {
    abstract fun getStackCopy(): S

    abstract fun setStack(stack: S)

    protected abstract fun setStackInternal(stack: S)

    protected abstract fun getResourceFrom(stack: S): T

    protected abstract fun getAmountFrom(stack: S): Int

    protected abstract fun isSame(stack: S, resource: T): Boolean

    protected abstract fun createStack(resource: T, amount: Int): S

    /**
     * 指定したリソースをこのスロットに搬入できるか判定します。
     * @param resource 搬入されるリソース
     * @param access このスロットへのアクセスの種類
     * @return 搬入できる場合は`true`
     */
    open fun isStackValidForInsert(resource: T, access: HTTransferAccess): Boolean = isValid(resource)

    /**
     * 指定したリソースをこのスロットから搬出できるか判定します。
     * @param resource 搬出されるリソース
     * @param access このスロットへのアクセスの種類
     * @return 搬出できる場合は`true`
     */
    open fun canStackExtract(resource: T, access: HTTransferAccess): Boolean = true

    /**
     * 一度に搬入される量の上限を返します。
     * @param access このスロットへのアクセスの種類
     */
    protected open fun inputRate(access: HTTransferAccess): Int = Int.MAX_VALUE

    /**
     * 一度に搬出される量の上限を返します。
     * @param access このスロットへのアクセスの種類
     */
    protected open fun outputRate(access: HTTransferAccess): Int = Int.MAX_VALUE

    //    HTResourceSlot    //

    override fun insert(resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        val amountIn: Int = this.amount
        if (amountIn == 0 || isSame(getStackCopy(), resource)) {
            if (isStackValidForInsert(resource, access)) {
                val needed: Int = minOf(inputRate(access), getNeeded(resource))
                val inserted: Int = minOf(amount, needed)
                if (inserted > 0) {
                    this.updateSnapshots(transaction)
                    setStackInternal(createStack(resource, amountIn + inserted))
                    return inserted
                }
            }
        }
        return 0
    }

    override fun extract(resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        if (isSame(getStackCopy(), resource) && canStackExtract(resource, access)) {
            val amountIn: Int = this.amount
            val stored: Int = minOf(outputRate(access), amountIn)
            val extracted: Int = minOf(amount, stored)
            if (extracted > 0) {
                this.updateSnapshots(transaction)
                setStackInternal(createStack(resource, amountIn - extracted))
                return extracted
            }
        }
        return 0
    }

    final override val resource: T get() = getStackCopy().let(::getResourceFrom)

    final override val amount: Int get() = getStackCopy().let(::getAmountFrom)

    //    SnapshotJournal    //

    final override fun createSnapshot(): S = getStackCopy()

    final override fun revertToSnapshot(snapshot: S) {
        setStackInternal(snapshot)
    }

    abstract override fun onRootCommit(originalState: S)

    //    Basic    //

    abstract class Basic<S : Any, T : Resource>(
        protected val capacity: Int,
        private val canExtract: BiPredicate<T, HTTransferAccess>,
        private val canInsert: BiPredicate<T, HTTransferAccess>,
        private val filter: Predicate<T>,
        private val listener: Runnable?,
    ) : HTStackResourceSlot<S, T>() {
        override fun isStackValidForInsert(resource: T, access: HTTransferAccess): Boolean = super.isStackValidForInsert(resource, access) && canInsert.test(resource, access)

        override fun canStackExtract(resource: T, access: HTTransferAccess): Boolean = super.canStackExtract(resource, access) && canExtract.test(resource, access)

        override fun onRootCommit(originalState: S) {
            listener?.run()
        }

        override fun isValid(resource: T): Boolean = filter.test(resource)

        override fun getCapacity(resource: T): Int = capacity
    }
}
