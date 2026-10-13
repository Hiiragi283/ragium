package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.util.fixedFraction
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * Hiiragi Seriesで使用される[EnergyHandler]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
interface HTEnergyHandler : EnergyHandler {
    fun insert(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    @Deprecated(
        "Use 'insert(Int, TransactionContext, HTTransferAccess)' instead",
        ReplaceWith("this.insert(amount, transaction, HTTransferAccess.EXTERNAL)")
    )
    override fun insert(amount: Int, transaction: TransactionContext): Int =
        insert(amount, transaction, HTTransferAccess.EXTERNAL)

    fun extract(amount: Int, transaction: TransactionContext, access: HTTransferAccess): Int

    @Deprecated(
        "Use 'extract(Int, TransactionContext, HTTransferAccess)' instead",
        ReplaceWith("this.extract(amount, transaction, HTTransferAccess.EXTERNAL)")
    )
    override fun extract(amount: Int, transaction: TransactionContext): Int =
        extract(amount, transaction, HTTransferAccess.EXTERNAL)

    val isEmpty: Boolean get() = amountAsLong == 0L

    val neededAsLong: Long get() = capacityAsLong - amountAsLong
    val filledLevelAsDouble: Double get() = fixedFraction(amountAsLong, capacityAsLong)

    val neededAsInt: Int get() = capacityAsInt - amountAsInt
    val filledLevelAsFloat: Float get() = fixedFraction(amountAsInt, capacityAsInt)
}
