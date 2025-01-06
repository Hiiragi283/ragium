package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.LimitingEnergyStorage

/**
 * ストレージの搬入出を管理するクラス
 */
enum class HTStorageIO(val canInsert: Boolean, val canExtract: Boolean) {
    INPUT(true, false),
    OUTPUT(false, true),
    GENERIC(true, true),
    INTERNAL(false, false),
    ;

    /**
     * 指定した[storage]の搬入出を制限した[Storage]を返します。
     * @see [FilteringStorage]
     */
    fun <T : Any> wrapStorage(storage: Storage<T>): Storage<T> = when (this) {
        INPUT -> FilteringStorage.insertOnlyOf(storage)
        OUTPUT -> FilteringStorage.extractOnlyOf(storage)
        GENERIC -> storage
        INTERNAL -> FilteringStorage.readOnlyOf(storage)
    }

    /**
     * 指定した[view]の搬出を制限した[StorageView]を返します。
     */
    fun <T : Any> wrapView(view: StorageView<T>): StorageView<T> = when (canExtract) {
        true -> view
        false -> object : StorageView<T> {
            override fun extract(resource: T?, maxAmount: Long, transaction: TransactionContext?): Long = 0

            override fun isResourceBlank(): Boolean = view.isResourceBlank

            override fun getResource(): T? = view.resource

            override fun getAmount(): Long = view.amount

            override fun getCapacity(): Long = view.capacity
        }
    }

    /**
     * 指定した[storage]の搬入出を制限した[EnergyStorage]を返します。
     * @see LimitingEnergyStorage
     */
    fun wrapEnergyStorage(storage: EnergyStorage): EnergyStorage = when (this) {
        INPUT -> LimitingEnergyStorage(storage, Long.MAX_VALUE, 0)
        OUTPUT -> LimitingEnergyStorage(storage, 0, Long.MAX_VALUE)
        GENERIC -> storage
        INTERNAL -> LimitingEnergyStorage(storage, 0, 0)
    }
}
