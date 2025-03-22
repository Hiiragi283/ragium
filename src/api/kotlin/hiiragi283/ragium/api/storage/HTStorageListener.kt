package hiiragi283.ragium.api.storage

fun interface HTStorageListener : () -> Unit {
    fun onContentsChanged()

    override fun invoke() {
        onContentsChanged()
    }
}
