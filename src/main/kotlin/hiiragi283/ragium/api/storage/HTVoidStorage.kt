package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext

/**
 * 中身を常に消去する[InsertionOnlyStorage]の実装
 */
class HTVoidStorage<T : Any> : InsertionOnlyStorage<T> {
    companion object {
        /**
         * [ItemVariant]に対する[HTVoidStorage]のインスタンス
         */
        @JvmField
        val ITEM: HTVoidStorage<ItemVariant> = HTVoidStorage()

        /**
         * [FluidVariant]に対する[HTVoidStorage]のインスタンス
         */
        @JvmField
        val FLUID: HTVoidStorage<FluidVariant> = HTVoidStorage()
    }

    override fun insert(resource: T, maxAmount: Long, transaction: TransactionContext): Long = maxAmount
}
