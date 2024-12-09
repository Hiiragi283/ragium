package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext

class HTVoidStorage<T : Any> : InsertionOnlyStorage<T> {
    companion object {
        @JvmField
        val ITEM: HTVoidStorage<ItemVariant> = HTVoidStorage()

        @JvmField
        val FLUID: HTVoidStorage<FluidVariant> = HTVoidStorage()
    }

    override fun insert(resource: T, maxAmount: Long, transaction: TransactionContext): Long = maxAmount
}
