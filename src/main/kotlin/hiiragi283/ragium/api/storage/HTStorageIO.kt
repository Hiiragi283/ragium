package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage

enum class HTStorageIO(val canInsert: Boolean, val canExtract: Boolean) {
    INPUT(true, false),
    OUTPUT(false, true),
    GENERIC(true, true),
    INTERNAL(false, false),
    ;

    fun <T : Any> wrapStorage(storage: Storage<T>): Storage<T> = when (this) {
        INPUT -> FilteringStorage.insertOnlyOf(storage)
        OUTPUT -> FilteringStorage.extractOnlyOf(storage)
        GENERIC -> storage
        INTERNAL -> FilteringStorage.readOnlyOf(storage)
    }
}
