package hiiragi283.ragium.api.storage

interface HTStorageView<T : Any> {
    val resource: T
    val amount: Int
    val capacity: Int
    val isEmpty: Boolean
}
