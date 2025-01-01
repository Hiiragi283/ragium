package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
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
