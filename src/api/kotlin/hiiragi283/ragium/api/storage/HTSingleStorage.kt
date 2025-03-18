package hiiragi283.ragium.api.storage

interface HTSingleStorage<T : Any> :
    HTStorage<T>,
    HTStorageView<T> {
    override fun iterator(): Iterator<HTStorageView<T>> = listOf(this).iterator()
}
