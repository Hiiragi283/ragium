package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * スタックをスナップショットとして保存する[HTStackSlot]の抽象クラスです。
 * @param S リソースと量を束ねたクラス
 * @param T 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTSnapshotStackResourceSlot<S : Any, T : Resource> :
    SnapshotJournal<S>(),
    HTStackSlot<S, T> {
    protected abstract fun getHelper(): HTStackResourceHelper<S, T>

    /**
     * 保持しているスタックを置き換えます。
     */
    protected abstract fun setStackInternal(stack: S)

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
        if (amountIn == 0 || getHelper().isSame(getStackCopy(), resource)) {
            if (isValidForInsert(resource, access)) {
                val needed: Int = minOf(inputRate(access), getNeeded(resource))
                val inserted: Int = minOf(amount, needed)
                if (inserted > 0) {
                    this.updateSnapshots(transaction)
                    setStackInternal(getHelper().createStack(resource, amountIn + inserted))
                    return inserted
                }
            }
        }
        return 0
    }

    override fun extract(resource: T, amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        if (getHelper().isSame(getStackCopy(), resource) && canResourceExtract(resource, access)) {
            val amountIn: Int = this.amount
            val stored: Int = minOf(outputRate(access), amountIn)
            val extracted: Int = minOf(amount, stored)
            if (extracted > 0) {
                this.updateSnapshots(transaction)
                setStackInternal(getHelper().createStack(resource, amountIn - extracted))
                return extracted
            }
        }
        return 0
    }

    final override val resource: T get() = getStackCopy().let(getHelper()::getResourceFrom)

    final override val amount: Int get() = getStackCopy().let(getHelper()::getAmountFrom)

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
    ) : HTSnapshotStackResourceSlot<S, T>() {
        override fun isValidForInsert(resource: T, access: HTTransferAccess): Boolean =
            super.isValidForInsert(resource, access) && canInsert.test(resource, access)

        override fun canResourceExtract(resource: T, access: HTTransferAccess): Boolean =
            super.canResourceExtract(resource, access) && canExtract.test(resource, access)

        override fun onRootCommit(originalState: S) {
            listener?.run()
        }

        override fun isValid(resource: T): Boolean = filter.test(resource)

        override fun getCapacity(resource: T): Int = capacity
    }
}
