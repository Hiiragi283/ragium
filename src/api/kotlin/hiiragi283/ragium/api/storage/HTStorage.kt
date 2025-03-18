package hiiragi283.ragium.api.storage

interface HTStorage<T : Any> : Iterable<HTStorageView<T>> {
    fun insert(resource: T, maxAmount: Int, simulate: Boolean): Int

    fun extract(resource: T, maxAmount: Int, simulate: Boolean): Int
}
