package hiiragi283.lib.transfer.energy

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.HTTransferValidators
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.Predicate

open class HTBasicEnergyStorage(
    private val capacityIn: Int,
    protected val canExtract: Predicate<HTTransferAccess>,
    protected val canInsert: Predicate<HTTransferAccess>,
    private val listener: Runnable?,
) : SnapshotJournal<Int>(),
    HTEnergyStorage {
    companion object {
        @JvmStatic
        fun input(capacity: Int, listener: Runnable?): HTBasicEnergyStorage =
            create(capacity, listener, HTTransferAccess.NOT_EXTERNAL, HTTransferPredicates.alwaysTrue())

        @JvmStatic
        fun output(capacity: Int, listener: Runnable?): HTBasicEnergyStorage =
            create(capacity, listener, HTTransferPredicates.alwaysTrue(), HTTransferAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            capacity: Int,
            listener: Runnable?,
            canExtract: Predicate<HTTransferAccess> = HTTransferPredicates.alwaysTrue(),
            canInsert: Predicate<HTTransferAccess> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicEnergyStorage = HTBasicEnergyStorage(HTTransferValidators.validateCapacity(capacity), canExtract, canInsert, listener)
    }

    private var amountIn: Int = 0

    final override val amount: Int get() = this.amountIn

    fun setAmount(newAmount: Int, transaction: TransactionContext? = null) {
        TransferPreconditions.checkNonNegative(newAmount)
        if (newAmount != this.amountIn) {
            transaction?.let {
                updateSnapshots(it)
                this.amountIn = newAmount
            } ?: run {
                val oldAmount: Int = this.amountIn
                this.amountIn = newAmount
                onRootCommit(oldAmount)
            }
        }
    }

    override val capacity: Int get() = this.capacityIn

    override fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (!this.canInsert.test(access)) return 0
        val inserted: Int = minOf(needed, amount)
        if (inserted > 0) {
            setAmount(amountIn + inserted, transaction)
            return inserted
        }
        return 0
    }

    override fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (!this.canExtract.test(access)) return 0
        val extracted: Int = minOf(this.amountIn, amount)
        if (extracted > 0) {
            setAmount(amountIn - extracted, transaction)
            return extracted
        }
        return 0
    }

    //    ValueIOSerializable    //

    override fun serialize(output: ValueOutput) {
        output.putInt(HTConstants.AMOUNT, amountIn)
    }

    override fun deserialize(input: ValueInput) {
        input.getInt(HTConstants.AMOUNT).ifPresent(::setAmount)
    }

    //    SnapshotJournal    //

    override fun createSnapshot(): Int = amountIn

    override fun revertToSnapshot(snapshot: Int) {
        this.amountIn = snapshot
    }

    override fun onRootCommit(originalState: Int) {
        if (this.amountIn != originalState) {
            listener?.run()
        }
    }

    override fun toString(): String = "HTBasicEnergyStorage(amount=$amountIn, capacity=$capacity)"
}
