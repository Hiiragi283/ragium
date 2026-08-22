package hiiragi283.lib.transfer.energy

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.HTTransferValidators
import java.util.function.Predicate
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext

open class HTBasicEnergyHandler(
    override val capacity: Int,
    protected val canExtract: Predicate<HTTransferAccess>,
    protected val canInsert: Predicate<HTTransferAccess>,
    private val listener: Runnable?,
) : SnapshotJournal<Int>(),
    HTEnergyHandler,
    ValueIOSerializable {
    companion object {
        @JvmStatic
        fun input(listener: Runnable?, capacity: Int): HTBasicEnergyHandler = create(listener, capacity, HTTransferAccess.NOT_EXTERNAL, HTTransferPredicates.alwaysTrue())

        @JvmStatic
        fun output(listener: Runnable?, capacity: Int): HTBasicEnergyHandler = create(listener, capacity, HTTransferPredicates.alwaysTrue(), HTTransferAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            listener: Runnable?,
            capacity: Int,
            canExtract: Predicate<HTTransferAccess> = HTTransferPredicates.alwaysTrue(),
            canInsert: Predicate<HTTransferAccess> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicEnergyHandler = HTBasicEnergyHandler(HTTransferValidators.validateCapacity(capacity), canExtract, canInsert, listener)
    }

    private var amountIn: Int = 0

    override val amount: Int get() = amountIn

    fun setAmount(amount: Int) {
        setAmountUnchecked(amount, true)
    }

    protected fun setAmountInternal(amount: Int) {
        setAmountUnchecked(amount, false)
    }

    protected fun setAmountUnchecked(amount: Int, validate: Boolean) {
        if (amount == 0) {
            if (this.amountIn == 0) return
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
        val inserted: Int = minOf(needed, amount)
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
        val extracted: Int = minOf(this.amountIn, amount)
        if (extracted > 0) {
            this.updateSnapshots(transaction)
            amountIn -= extracted
            return extracted
        }
        return 0
    }

    //    ValueIOSerializable    //

    override fun serialize(output: ValueOutput) {
        output.putInt(HTConstants.AMOUNT, amountIn)
    }

    override fun deserialize(input: ValueInput) {
        input.getInt(HTConstants.AMOUNT).ifPresent(::setAmountInternal)
    }

    //    SnapshotJournal    //

    override fun createSnapshot(): Int = amountIn

    override fun revertToSnapshot(snapshot: Int) {
        this.amountIn = snapshot
    }

    override fun onRootCommit(originalState: Int) {
        listener?.run()
    }

    override fun toString(): String = "HTBasicEnergyHandler(amount=$amountIn, capacity=$capacity)"
}
