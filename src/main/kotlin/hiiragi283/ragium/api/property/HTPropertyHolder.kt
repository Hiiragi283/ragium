package hiiragi283.ragium.api.property

interface HTPropertyHolder : Iterable<Pair<HTPropertyKey<*>, Any>> {
    operator fun <T : Any> get(id: HTPropertyKey<T>): T?

    fun <T : Any> getOrDefault(id: HTPropertyKey.Defaulted<T>): T = get(id) ?: id.getDefaultValue()

    fun <T : Any> getOrThrow(id: HTPropertyKey<T>): T = get(id) ?: throw IllegalStateException("")

    operator fun contains(id: HTPropertyKey<*>): Boolean

    fun <T : Any> ifPresent(id: HTPropertyKey<T>, action: (T) -> Unit) {
        get(id)?.let(action)
    }

    //    Empty    //

    object Empty : HTPropertyHolder {
        override fun <T : Any> get(id: HTPropertyKey<T>): T? = null

        override fun contains(id: HTPropertyKey<*>): Boolean = false

        override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = listOf<Pair<HTPropertyKey<*>, Any>>().iterator()
    }
}
