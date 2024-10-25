package hiiragi283.ragium.api.energy

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext

object HTCreativeEnergyStorage : HTSingleEnergyStorage() {
    override fun supportsInsertion(): Boolean = false

    override fun insert(insertedVariant: HTEnergyType, maxAmount: Long, transaction: TransactionContext): Long = 0

    override fun extract(extractedVariant: HTEnergyType, maxAmount: Long, transaction: TransactionContext): Long = maxAmount

    override fun getAmount(): Long = Long.MAX_VALUE

    override fun getCapacity(variant: HTEnergyType): Long = Long.MAX_VALUE
}
