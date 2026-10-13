package hiiragi283.lib.transfer.energy

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.HTTransferValidators
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.Predicate

open class HTBasicEnergyHandler(
    val capacity: Long,
    protected val canExtract: Predicate<HTTransferAccess>,
    protected val canInsert: Predicate<HTTransferAccess>,
    private val listener: Runnable?
) : SnapshotJournal<Long>(),
    HTEnergyHandler,
    ValueIOSerializable {
    companion object {
        @JvmStatic
        fun input(capacity: Long, listener: Runnable?): HTBasicEnergyHandler =
            create(capacity, listener, HTTransferAccess.NOT_EXTERNAL, HTTransferPredicates.alwaysTrue())

        @JvmStatic
        fun output(capacity: Long, listener: Runnable?): HTBasicEnergyHandler =
            create(capacity, listener, HTTransferPredicates.alwaysTrue(), HTTransferAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            capacity: Long,
            listener: Runnable?,
            canExtract: Predicate<HTTransferAccess> = HTTransferPredicates.alwaysTrue(),
            canInsert: Predicate<HTTransferAccess> = HTTransferPredicates.alwaysTrue()
        ): HTBasicEnergyHandler =
            HTBasicEnergyHandler(HTTransferValidators.validateCapacity(capacity), canExtract, canInsert, listener)
    }

    private var amountIn: Long = 0

    override fun getAmountAsLong(): Long = amountIn

    override fun getCapacityAsLong(): Long = capacity

    fun setAmount(amount: Long) {
        setAmountUnchecked(amount, true)
    }

    protected fun setAmountInternal(amount: Long) {
        setAmountUnchecked(amount, false)
    }

    protected fun setAmountUnchecked(amount: Long, validate: Boolean) {
        if (amount == 0L) {
            if (this.amountIn == 0L) return
            this.amountIn = 0
        } else if (!validate || amount > 0) {
            this.amountIn = amount.coerceIn(0, capacity)
        } else {
            error("Invalid amount for storage: $amount")
        }
        onRootCommit(this.amountIn)
    }

    override fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (!this.canInsert.test(access)) return 0
        val inserted: Int = minOf(neededAsInt, amount)
        if (inserted > 0) {
            this.updateSnapshots(transaction)
            amountIn += inserted
            return inserted
        }
        return 0
    }

    override fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (!this.canExtract.test(access)) return 0
        val extracted: Int = minOf(this.amountAsInt, amount)
        if (extracted > 0) {
            this.updateSnapshots(transaction)
            amountIn -= extracted
            return extracted
        }
        return 0
    }

    //    ValueIOSerializable    //

    override fun serialize(output: ValueOutput) {
        output.putLong(HTConstants.AMOUNT, amountIn)
    }

    override fun deserialize(input: ValueInput) {
        input.getLong(HTConstants.AMOUNT).ifPresent(::setAmountInternal)
    }

    //    SnapshotJournal    //

    override fun createSnapshot(): Long = amountIn

    override fun revertToSnapshot(snapshot: Long) {
        this.amountIn = snapshot
    }

    override fun onRootCommit(originalState: Long) {
        listener?.run()
    }

    override fun toString(): String = "HTBasicEnergyHandler(amount=$amountIn, capacity=$capacity)"
}
