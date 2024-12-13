package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext

/**
 * A simple [InsertionOnlyStorage] implementation which removes all inserted resource
 */
class HTVoidStorage<T : Any> : InsertionOnlyStorage<T> {
    companion object {
        /**
         * [HTVoidStorage] for [ItemVariant]
         */
        @JvmField
        val ITEM: HTVoidStorage<ItemVariant> = HTVoidStorage()

        /**
         * [HTVoidStorage] for [FluidVariant]
         */
        @JvmField
        val FLUID: HTVoidStorage<FluidVariant> = HTVoidStorage()
    }

    override fun insert(resource: T, maxAmount: Long, transaction: TransactionContext): Long = maxAmount
}
